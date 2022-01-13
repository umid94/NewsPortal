package uz.umid.task.newsportal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.umid.task.newsportal.model.News;
import uz.umid.task.newsportal.model.User;
import uz.umid.task.newsportal.repository.NewsRepository;
import uz.umid.task.newsportal.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class NewsService {
    private static final String APPROVED = "approved";
    private static final String DISAPPROVED = "disapproved";
    private static final String REFUSED = "refused";


    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    public NewsService(NewsRepository newsRepository, UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
    }

    public News getById(Long id) {
        return newsRepository.getById(id);
    }

    public void approvedNews(Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            News news1 = news.get();
            news1.setStatus(APPROVED);
            news1.setApprovedDate(new Date());
            newsRepository.save(news1);
        }
    }

    public void refusedNews(Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            News news1 = news.get();
            news1.setStatus(REFUSED);
            news1.setApprovedDate(new Date());
            newsRepository.save(news1);
        }
    }

    public void saveNews(News news) {
        if (news.getId() != null) {
            News news1 = newsRepository.getById(news.getId());
            news1.setTitle(news.getTitle());
            news1.setContent(news.getContent());
            news1.setStatus(DISAPPROVED);
            news1.setCreateDate(new Date());
            save(news1);
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> user = userRepository.findByUsername(auth.getName());
            news.setUser(user.get());
            news.setStatus(DISAPPROVED);
            news.setCreateDate(new Date());
            save(news);
        }
    }

    public void save(News news) {
        newsRepository.save(news);
    }

    public Page<News> getAllUserNews(int pageNumber, int pageSize) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return newsRepository.getAllUserNews(username, pageable);
    }

    public Page<News> findPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return newsRepository.findAll(pageable);
    }

    public Page<News> findAllApprovedNewsWithPagination(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        return newsRepository.findAllApprovedNewsWithPagination(pageable);
    }
}
