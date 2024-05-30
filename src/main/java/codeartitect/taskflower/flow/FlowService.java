package codeartitect.taskflower.flow;

import codeartitect.taskflower.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlowService {

    private final FlowRepository flowRepository;

    @Autowired
    public FlowService(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    /**
     * Save flow
     * @param user User information
     * @param saveFlowRequest Information to save flow
     * @return FlowResponse
     */
    public FlowResponse save(User user, SaveFlowRequest saveFlowRequest) {
        Flow flow = new Flow(user, saveFlowRequest);

        flowRepository.save(flow);

        return new FlowResponse(flow);
    }

    /**
     * Get flow by flow id
     * @param id Flow id
     * @return FlowResponse
     */
    public FlowResponse getById(Long id) {
        Flow flow = flowRepository.findById(id).orElseThrow(FlowNotFoundException::new);
        return new FlowResponse(flow);
    }

    /**
     * Find all flows by user info
     * @param user User information
     * @return FlowResponse list
     */
    public List<FlowResponse> findAll(User user) {
        List<Flow> flows = flowRepository.findAllByUser(user);

        List<FlowResponse> flowResponseList = new ArrayList<>();
        for (Flow flow : flows) {
            flowResponseList.add(new FlowResponse(flow));
        }
        return flowResponseList;
    }

    /**
     * Update flow
     * @param flowId Flow id
     * @param saveFlowRequest Information to update flow
     * @return FlowResponse
     */
    public FlowResponse updateFlow(Long flowId, SaveFlowRequest saveFlowRequest) {
        Flow flow = flowRepository.findById(flowId).orElseThrow(FlowNotFoundException::new);
        flow.update(saveFlowRequest);

        Flow updateFlow = flowRepository.save(flow);
        return new FlowResponse(updateFlow);
    }

    /**
     * Delete flow by flow id
     * @param id Flow id for delete
     */
    public void deleteById(Long id) {
        if (flowRepository.existsById(id)) {
            flowRepository.deleteById(id);
        } else throw new FlowNotFoundException();
    }
}
