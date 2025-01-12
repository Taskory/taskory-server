package codearchitect99.taskory.event;

import codearchitect99.taskory.common.util.TimeUtil;
import codearchitect99.taskory.event.payload.EventResponse;
import codearchitect99.taskory.event.payload.EventSummary;
import codearchitect99.taskory.event.payload.SaveEventRequest;
import codearchitect99.taskory.event.payload.TaskInEventDto;
import codearchitect99.taskory.hashtag.HashtagRepository;
import codearchitect99.taskory.tag.TagRepository;
import codearchitect99.taskory.task.model.Task;
import codearchitect99.taskory.task.repository.TaskRepository;
import codearchitect99.taskory.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final HashtagRepository hashtagRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public EventService(EventRepository eventRepository, TagRepository tagRepository, HashtagRepository hashtagRepository, TaskRepository taskRepository) {
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
        this.hashtagRepository = hashtagRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Save event
     * @param user User information
     * @param saveEventRequest Information to save event
     * @return EventResponse
     */
    @Transactional
    public EventResponse save(User user, SaveEventRequest saveEventRequest) {
        if (saveEventRequest.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }

        Event event = Event.builder()
                .title(saveEventRequest.getTitle())
                .description(saveEventRequest.getDescription())
                .startDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getStartDateTime()))
                .dueDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getDueDateTime()))
                .location(saveEventRequest.getLocation())
                .user(user)
                .build();

        if (saveEventRequest.getTagId() != null) {
            event.setTag(tagRepository.findById(saveEventRequest.getTagId()).orElse(null));
        } else {
            event.setTag(null);
        }
        if (saveEventRequest.getHashtagIds() != null) {
            event.setHashtags(hashtagRepository.findAllById(saveEventRequest.getHashtagIds()));
        } else {
            event.setHashtags(null);
        }

        eventRepository.save(event);

        if (saveEventRequest.getTasks() != null && !saveEventRequest.getTasks().isEmpty()) {
            List<Task> newTasks = saveEventRequest.getTasks().stream()
                    .map(taskDto ->
                            Task.builder()
                                    .user(user)
                                    .title(taskDto.getTitle())
                                    .event(event)
                                    .tag(event.getTag())
                                    .description("")
                                    .status(taskDto.getStatus())
                                    .build())
                    .toList();
            taskRepository.saveAll(newTasks);
            event.setTasks(new ArrayList<>(newTasks));
            eventRepository.save(event);
        }


        return new EventResponse(event);
    }

    /**
     * Get event by event id
     * @param id Event id
     * @return EventResponse
     */
    public EventResponse getById(Long id) throws EventNotFoundException {
        Event event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        return new EventResponse(event);
    }

    /**
     * Find all events by user info
     * @param user User information
     * @return EventResponse list
     */
    public List<EventSummary> findAll(User user) {
        List<Event> events = eventRepository.findAllByUser(user);

        List<EventSummary> eventResponseList = new ArrayList<>();
        for (Event event : events) {
            eventResponseList.add(new EventSummary(event));
        }
        return eventResponseList;
    }


    /**
     * Find events in period
     *
     * @param user user information
     * @param startDateString start date string -> format: yyyy-mm-ddThh:mm
     * @param endDateString end date string -> format: yyyy-mm-ddThh:mm
     * @return EventSummary list
     * @throws IllegalArgumentException if the endDate is before the startDate
     */
    public List<EventSummary> findEventsInPeriod(User user, String startDateString, String endDateString) {
        LocalDateTime startDateTime = TimeUtil.stringToLocalDateTime(startDateString);
        LocalDateTime endDateTime = TimeUtil.stringToLocalDateTime(endDateString);

        // Check if endDate is before startDate
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        List<Event> events = eventRepository.findAllByUserInPeriod(user, startDateTime, endDateTime);

        List<EventSummary> eventSummaryList = new ArrayList<>();
        for (Event event : events) {
            eventSummaryList.add(new EventSummary(event));
        }
        return eventSummaryList;
    }


    /**
     * Update event
     * @param eventId Event id
     * @param saveEventRequest Information to update event
     * @return EventResponse
     */
    @Transactional
    public EventResponse updateEvent(Long eventId, SaveEventRequest saveEventRequest) throws EventNotFoundException {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);

        /* Find existing tasks */
        List<Task> existingTasks = taskRepository.findByEvent(foundEvent);
        Map<Long, Task> existingTasksMap = existingTasks.stream()
                .collect(Collectors.toMap(Task::getId, task -> task));

        /* Create and Update */
        List<Task> newTaskList = new ArrayList<>();
        if (saveEventRequest.getTasks() != null && !saveEventRequest.getTasks().isEmpty()) {
            List<Long> requestExistingIdList = saveEventRequest.getTasks().stream()
                    .map(TaskInEventDto::getId)
                    .filter(Objects::nonNull)
                    .toList();

            for (TaskInEventDto task : saveEventRequest.getTasks()) {
                if (task.getId() != null && existingTasksMap.containsKey(task.getId())) {
                    Task updateTask = existingTasksMap.get(task.getId());
                    updateTask.setTitle(task.getTitle());
                    updateTask.setStatus(task.getStatus());

                    taskRepository.save(updateTask);
                } else {
                    Task newTask = Task.builder()
                            .user(foundEvent.getUser())
                            .title(task.getTitle())
                            .status(task.getStatus())
                            .tag(foundEvent.getTag())
                            .event(foundEvent)
                            .build();
                    newTaskList.add(newTask);
                }
            }

            List<Task> taskListToDelete = existingTasks.stream()
                    .filter(task -> !requestExistingIdList.contains(task.getId()))
                    .toList();

            taskRepository.deleteAll(taskListToDelete);

            existingTasks.removeAll(taskListToDelete);
        }

        foundEvent.getTasks().clear();
        foundEvent.getTasks().addAll(newTaskList);
        foundEvent.getTasks().addAll(existingTasks);

        if (saveEventRequest.getTagId() != null) {
            foundEvent.setTag(tagRepository.findById(saveEventRequest.getTagId()).orElse(null));
        } else {
            foundEvent.setTag(null);
        }
        if (saveEventRequest.getHashtagIds() != null) {
            foundEvent.setHashtags(hashtagRepository.findAllById(saveEventRequest.getHashtagIds()));
        } else {
            foundEvent.setHashtags(null);
        }
        foundEvent.setTitle(saveEventRequest.getTitle());
        foundEvent.setDescription(saveEventRequest.getDescription());
        foundEvent.setStartDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getStartDateTime()));
        foundEvent.setDueDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getDueDateTime()));
        foundEvent.setLocation(saveEventRequest.getLocation());



        Event result = eventRepository.save(foundEvent);
        return new EventResponse(result);
    }

    /**
     * Delete event by event id
     * @param id event id for delete
     */
    public void deleteById(Long id) throws EventNotFoundException {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else throw new EventNotFoundException();
    }


    /**
     * Find all events by tag ids
     * @param tagIds tag ids for find
     * @return EventSummary list
     */
    public List<EventSummary> findAllByTags(List<Long> tagIds) {
        List<Event> events = eventRepository.findByTag_IdIn(tagIds);
        return events.stream().map(EventSummary::new).toList();
    }

    /**
     * Find all upcoming and ongoing events
     * @param user user information
     * @param localDateTime time point for getting and upcoming
     * @return EventSummary list
     */
    public List<EventSummary> findUpcomingEvents(User user, LocalDateTime localDateTime) {
        List<Event> events = eventRepository.findOngoingAndUpcommingEvents(user, localDateTime);
        return events.stream().map(EventSummary::new).toList();
    }
}
