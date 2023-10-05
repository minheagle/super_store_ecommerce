package com.shopee.clone.DTO.comment;

import com.shopee.clone.DTO.auth.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String user;
    private String content;
    private List<CommentDTO> childComments;

}
