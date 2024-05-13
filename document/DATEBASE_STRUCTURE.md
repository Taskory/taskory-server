# Datebase Structure

## ENG

1. **Schedule Table**:
   - id: Unique identifier for the schedule.
   - parent_user: Identifier of the user who created the schedule.
   - name: Name of the schedule.
   - tag: Tag or category associated with the schedule.
   - hashtag: Hashtags related to the schedule.
   - description: Additional description or memo for the schedule.
   - start_date_time: Start date and time of the schedule.
   - end_date_time: End date and time of the schedule.
   - location: Location where the schedule takes place.
   - child_task_set: Identifier of the task set associated with the schedule (NULL allowed).
2. **Task Set Table**:
   - id: Unique identifier for the task set.
   - parent_user: Identifier of the user who created the task set.
   - name: Name of the task set.
   - hashtag: Hashtags related to the task set.
   - description: Additional description or memo for the task set.
   - parent_schedule: Identifier of the schedule associated with the task set (NULL allowed).
3. **Task Table**:
   - id: Unique identifier for the task.
   - parent_user: Identifier of the user who created the task.
   - name: Name of the task.
   - parent_task_set: Identifier of the task set associated with the task (NULL allowed).
   - tag: Tag or category associated with the task.
   - hashtag: Hashtags related to the task.
   - description: Additional description or memo for the task.
   - status: Status of the task (Todo, InProgress, Done, etc.).
   - datetime_start: Start date and time of the task.
4. Checklist Table:
   - id: Unique identifier for the checklist item.
   - parent_user: Identifier of the user who created the checklist item.
   - name: Name of the checklist item.
   - parent_task: Identifier of the task associated with the checklist item.
   - checked - boolean: Boolean indicating whether the checklist item is checked.
5. **Routine Table**:
   - id: Unique identifier for the routine.
   - parent_user: Identifier of the user who created the routine.
   - name: Name of the routine.
   - tag: Tag or category associated with the routine.
   - hashtag: Hashtags related to the routine.
   - description: Additional description or memo for the routine.
   - days: Days or frequency on which the routine occurs.
6. **Tag Table**:
   - id: Unique identifier for the tag.
   - parent_user: Identifier of the user who created the tag.
   - name: Name of the tag.
   - hashtag: Hashtags related to the tag.
   - description: Description of the tag.
7. **Hashtag Table**:
   - id: Unique identifier for the hashtag.
   - parent_user: Identifier of the user who created the hashtag.
   - name: Name of the hashtag.
8. **User Table**:
   - id: Unique identifier for the user.
   - username: Username of the user.
   - password: Password of the user.

## KOR

1. **Schedule 테이블**:
   - id: 일정의 고유 식별자
   - parent_user: 일정을 생성한 사용자의 식별자
   - name: 일정의 이름
   - tag: 일정에 대한 태그 또는 카테고리
   - hashtag: 일정의 해시태그
   - description: 일정에 대한 추가 설명 또는 메모
   - start_date_time: 일정 시작 일시
   - end_date_time: 일정 종료 일시
   - location: 일정이 진행되는 장소
   - child_task_set: 해당 일정과 연관된 작업 집합의 식별자 (NULL 허용)
2. **Task Set 테이블**:
   - id: 작업 집합의 고유 식별자
   - parent_user: 작업 집합을 생성한 사용자의 식별자
   - name: 작업 집합의 이름
   - hashtag: 작업 집합의 해시태그
   - description: 작업 집합에 대한 추가 설명 또는 메모
   - parent_schedule: 해당 작업 집합과 연관된 일정의 식별자 (NULL 허용)
3. **Task 테이블**:
   - id: 작업의 고유 식별자
   - parent_user: 작업을 생성한 사용자의 식별자
   - name: 작업의 이름
   - parent_task_set: 해당 작업과 연관된 작업 집합의 식별자 (NULL 허용)
   - tag: 작업에 대한 태그 또는 카테고리
   - hashtag: 작업의 해시태그
   - description: 작업에 대한 추가 설명 또는 메모
   - status: 작업의 상태 (Todo, InProgress, Done 등)
   - datetime_start: 작업의 시작 일시
4. Checklist 테이블
   1. id: 체크의 고유 식별자
   2. parent_user: 체크를 생성한 사용자의 식별자
   3. name: 체크의 이름
   4. parent_task: 해당 체크와 연관된 작업의 식별자
   5. checked - boolean: 체크 여부
5. **Routine 테이블**:
   - id: 루틴의 고유 식별자
   - parent_user: 루틴을 생성한 사용자의 식별자
   - name: 루틴의 이름
   - tag: 루틴에 대한 태그 또는 카테고리
   - hashtag: 루틴의 해시태그
   - description: 루틴에 대한 추가 설명 또는 메모
   - days: 루틴이 발생하는 요일 또는 주기
6. **Tag 테이블**:
   - id: 태그의 고유 식별자
   - parent_user: 태그를 생성한 사용자의 식별자
   - name: 태그의 이름
   - hashtag: 태그의 해시태그
   - description: 태그에 대한 설명
7. **Hashtag 테이블**:
   - id: 해시태그의 고유 식별자
   - parent_user: 해시태그를 생성한 사용자의 식별자
   - name: 해시태그의 이름
8. User 테이블

   - id

   - username

   - password

