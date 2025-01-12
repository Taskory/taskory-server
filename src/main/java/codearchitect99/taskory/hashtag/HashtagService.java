package codearchitect99.taskory.hashtag;

import codearchitect99.taskory.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    @Autowired
    public HashtagService(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    /**
     * Save hashtag
     * @param user User information
     * @param saveHashtagRequest Information to save hashtag
     * @return HashtagResponse
     */
    public HashtagResponse save(User user, SaveHashtagRequest saveHashtagRequest) {
        Hashtag hashtag = new Hashtag();
        hashtag.setUser(user);
        hashtag.setTitle(saveHashtagRequest.getTitle());

        hashtagRepository.save(hashtag);

        return new HashtagResponse(hashtag);
    }

    /**
     * Get hashtag by id
     * @param id Hashtag id
     * @return HashtagResponse
     */
    public HashtagResponse getById(Long id) throws HashtagNotFoundException {
        Hashtag hashtag = hashtagRepository.findById(id).orElseThrow(HashtagNotFoundException::new);
        return new HashtagResponse(hashtag);
    }

    /**
     * Find all hashtags by user info
     * @param user User information
     * @return List of HashtagResponse
     */
    public List<HashtagResponse> findAll(User user) {
        List<Hashtag> hashtags = hashtagRepository.findAllByUser(user);

        List<HashtagResponse> hashtagResponseList = new ArrayList<>();
        for (Hashtag hashtag : hashtags) {
            hashtagResponseList.add(new HashtagResponse(hashtag));
        }
        return hashtagResponseList;
    }

    /**
     * Update hashtag
     * @param hashtagId Hashtag id
     * @param saveHashtagRequest Information to update hashtag
     * @return HashtagResponse
     */
    public HashtagResponse updateHashtag(Long hashtagId, SaveHashtagRequest saveHashtagRequest) throws HashtagNotFoundException {
        Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(HashtagNotFoundException::new);
        hashtag.setTitle(saveHashtagRequest.getTitle());

        Hashtag updatedHashtag = hashtagRepository.save(hashtag);
        return new HashtagResponse(updatedHashtag);
    }

    /**
     * Delete hashtag by id
     * @param id Hashtag id for deletion
     */
    public void deleteById(Long id) throws HashtagNotFoundException {
        if (hashtagRepository.existsById(id)) {
            hashtagRepository.deleteById(id);
        } else throw new HashtagNotFoundException();
    }
}
