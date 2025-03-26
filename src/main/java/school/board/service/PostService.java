package school.board.service;

import org.springframework.stereotype.Service;
import school.board.domain.Post;
import school.board.repository.PostRepository;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void savePost(Post post){
        postRepository.save(post);
    }

    public Post findPost(Long id){
        return postRepository.findById(id);
    }

    public List<Post> findList(){
        return postRepository.findAll();
    }

    public void deletePost(Long id){
        Post delPost = postRepository.findById(id);
        if(delPost != null){
            postRepository.delete(delPost);
        }
    }
}
