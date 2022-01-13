package uz.umid.task.newsportal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.umid.task.newsportal.dto.NewsDTO;
import uz.umid.task.newsportal.model.News;
import uz.umid.task.newsportal.service.NewsService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private static final int PAGE_SIZE = 15;
    private static final String CURRENT_PAGE = "currentPage";
    private static final String TOTAL_PAGES = "totalPages";
    private static final String TOTAL_ITEMS = "totalItems";
    private static final String LIST_NEWS = "listNews";

    private final NewsService newsService;

    @GetMapping("main-page")
    public String getSuccessPage(Model model, Authentication authentication) {
        String list = String.valueOf(authentication.getAuthorities());
        if (list.contentEquals("[ROLE_ADMIN]")) {
            return findPaginated(1, model);
        } else {
            return findApprovedNewsPaginated(1, model);
        }

    }

    @GetMapping("admin/{pageNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String findPaginated(@PathVariable("pageNumber") int pageNumber, Model model) {

        Page<News> page = newsService.findPaginated(pageNumber, PAGE_SIZE);
        List<News> listNews = page.getContent();
        List<NewsDTO> newsDTOs = new ArrayList<>(listNews.size());
        for (News news : listNews) {
            NewsDTO newsDTO = new NewsDTO(news);
            newsDTOs.add(newsDTO);
        }

        model.addAttribute(CURRENT_PAGE, pageNumber);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());

        model.addAttribute(LIST_NEWS, newsDTOs);
        return "main";
    }

    @GetMapping("main-page/{pageNumber}")
    public String findApprovedNewsPaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model) {

        Page<News> page = newsService.findAllApprovedNewsWithPagination(pageNumber, PAGE_SIZE);
        List<News> listNews = page.getContent();
        List<NewsDTO> newsDTOs = new ArrayList<>(listNews.size());
        for (News news : listNews) {
            NewsDTO newsDTO = new NewsDTO(news.getId(), news.getTitle(), news.getContent());
            newsDTOs.add(newsDTO);
        }
        model.addAttribute(CURRENT_PAGE, pageNumber);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());

        model.addAttribute(LIST_NEWS, newsDTOs);
        return "main";
    }

    @GetMapping("main-page/my-news/{pageNumber}")
    public String findMyNewsPaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model) {

        Page<News> page = newsService.getAllUserNews(pageNumber, PAGE_SIZE);
        List<News> listNews = page.getContent();
        List<NewsDTO> newsDTOs = new ArrayList<>(listNews.size());
        for (News news : listNews) {
            NewsDTO newsDTO = new NewsDTO(news.getId(), news.getTitle(), news.getContent());
            newsDTOs.add(newsDTO);
        }

        model.addAttribute(CURRENT_PAGE, pageNumber);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());

        model.addAttribute(LIST_NEWS, newsDTOs);
        return "my-news";
    }

    @GetMapping("admin/{pageNumber}/news/approved/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String approvedNews(@PathVariable("id") Long id, @PathVariable("pageNumber") int page) {
        newsService.approvedNews(id);
        return "redirect:/admin/" + page;
    }

    @GetMapping("admin/{pageNumber}/news/refused/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String refusedNews(@PathVariable("id") Long id, @PathVariable("pageNumber") int page) {
        newsService.refusedNews(id);
        return "redirect:/admin/" + page;
    }

    @GetMapping("main-page/news/add")
    public String newsForm(Model model) {
        model.addAttribute("oneNews", new News());
        return "newsForm";
    }

    @PostMapping("main-page/news/save")
    public String newsForm(@Valid @ModelAttribute("news") News news, BindingResult theBindingResult) {
        if (theBindingResult.hasErrors()) {
            return "newsForm";
        } else {
            System.out.println(news.getId());
            newsService.saveNews(news);
            return "redirect:/main-page/my-news/1";
        }
    }

    @GetMapping("main-page/my-news/update/{id}")
    public String updateNews(@PathVariable("id") Long id, Model model) {
        model.addAttribute("oneNews", newsService.getById(id));
        return "newsForm";
    }
}
