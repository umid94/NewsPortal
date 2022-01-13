package uz.umid.task.newsportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uz.umid.task.newsportal.model.News;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends CrudRepository<News, Long>, PagingAndSortingRepository<News, Long> {

    @Query("SELECT n FROM News n where n.user.username = ?1")
    Page<News> getAllUserNews(String status, Pageable pageable);

    Optional<News> findById(Long id);

    @Query("SELECT n FROM News n WHERE n.user.username = ?1")
    List<News> getAllMyNews(String username);

    @Query("SELECT n FROM News n where n.id = ?1")
    News getById(Long id);

    @Query(value = "SELECT * FROM news WHERE status = 'approved' ORDER BY id",
            countQuery = "SELECT count(*) FROM news WHERE status = 'approved'",
            nativeQuery = true)
    Page<News> findAllApprovedNewsWithPagination(Pageable pageable);
}
