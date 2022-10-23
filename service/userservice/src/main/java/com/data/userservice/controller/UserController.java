package com.data.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.data.common.BaseResponse;
import com.data.common.ErrorCode;
import com.data.common.ResultUtils;
import com.data.exception.BusinessException;
import com.data.userservice.model.entity.User;
import com.data.userservice.model.request.UserLoginRequest;
import com.data.userservice.model.request.UserRegisterRequest;
import com.data.userservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String tokenKey = userService.userLoginByToken(userLoginRequest);
        return ResultUtils.success(tokenKey);
    }

    @PostMapping("/current")
    public BaseResponse<User> userLogin(HttpServletRequest request) {
        if(request == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = userService.getCurrentUser(request);
        return ResultUtils.success(user);
    }


//    @PostMapping("/login")
//    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
//        if (userLoginRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        String userAccount = userLoginRequest.getUserAccount();
//        String userPassword = userLoginRequest.getUserPassword();
//        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User user = userService.userLogin(userAccount, userPassword, request);
//        return ResultUtils.success(user);
//    }

//    @PostMapping("/logout")
//    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
//        if (request == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        int result = userService.userLogout(request);
//        return ResultUtils.success(result);
//    }

//    @GetMapping("/current")
//    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
//        User loginUser = userService.getLoginUser(request);
//        long userId = loginUser.getId();
//        User user = userService.getById(userId);
//        User safetyUser = userService.getSafetyUser(user);
//        return ResultUtils.success(safetyUser);
//    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        userService.assertAdmin(request);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        userService.assertAdmin(request);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @GetMapping("/test")
    public BaseResponse<String> ping(){
        return ResultUtils.success("成功找到user-service模块");
    }

}
