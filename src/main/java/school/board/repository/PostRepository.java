package school.board.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import school.board.domain.Post;

import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Post post) {
        if(post.getId() == null){
            em.persist(post);
        }
        else{
            em.merge(post);
        }
    }

    public Post findById(Long id){
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("SELECT p FROM Post p ORDER BY p.createdDate DESC", Post.class)
                .getResultList();
    }

    public void delete(Post post){
        em.remove(post);
    }
}
