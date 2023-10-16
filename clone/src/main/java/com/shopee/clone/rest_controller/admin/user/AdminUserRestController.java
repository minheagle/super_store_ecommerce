package com.shopee.clone.rest_controller.admin.user;

import com.shopee.clone.service.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/users")
public class AdminUserRestController {
    private final UserService userService;

    public AdminUserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list-user")
    public ResponseEntity<?> getListUser(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                         @RequestParam(name = "size", required = false, defaultValue = "25") Integer size,
                                         @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of((page - 1), size, sortable);
        return userService.getListUser(pageable);
    }
    @GetMapping("/list-seller")
    public ResponseEntity<?> getListSeller(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                           @RequestParam(name = "size", required = false, defaultValue = "25") Integer size,
                                           @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of((page - 1), size, sortable);
        return userService.getListSeller(pageable);
    }
    @GetMapping("/ban-user/{id}")
    public ResponseEntity<?> banUser(@PathVariable Long id){
        return userService.blockUser(id);
    }
    @GetMapping("search")
    public ResponseEntity<?> searchAndFilter(@RequestParam(name="key", required = false, defaultValue = "") String key,
                                             @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                             @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of((page-1), size, sortable);

        return  userService.search(key, pageable);
    }
    @GetMapping("/unban-user/{id}")
    public ResponseEntity<?> unBanUser(@PathVariable Long id){
        return userService.unBlockUser(id);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName){
        return userService.findUserByUserName(userName);
    }
}
