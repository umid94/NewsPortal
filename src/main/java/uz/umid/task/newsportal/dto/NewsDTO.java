package uz.umid.task.newsportal.dto;

import lombok.Data;
import uz.umid.task.newsportal.model.News;

import java.util.Date;

@Data
public class NewsDTO {
    private Long id;
    private String title;
    private String content;
    private Date createDate;
    private Date approvedDate;
    private String status;
    private String login;

    public NewsDTO(News news){
        id = news.getId();
        title = news.getTitle();
        content = news.getContent();
        status = news.getStatus();
        createDate = news.getCreateDate();
        approvedDate = news.getApprovedDate();
        login = news.getUser().getUsername();
    }

    public NewsDTO(Long id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
