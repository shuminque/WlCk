package com.depository_manage.controller;

import com.depository_manage.entity.Category;
import com.depository_manage.entity.Notice;
import com.depository_manage.exception.MyException;
import com.depository_manage.service.*;
import com.depository_manage.pojo.DepositoryRecordP;
import com.depository_manage.security.bean.UserToken;
import com.depository_manage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
public class PageController {

    @Autowired
    private DepositoryService depositoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private DepositoryRecordService depositoryRecordService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private MaterialEnginService materialEnginService;
    @Autowired
    private MaterialTypeService materialTypeService;
    @Autowired
    private MaterialStateService materialStateService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/login")
    public String login() {
        return "pages/user/login";
    }

    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        mv.addObject("uname", userToken.getUser().getUname());
        mv.setViewName("index");
        return mv;
    }

    @GetMapping("/register")
    public String register() {
        return "pages/user/register";
    }
    public String formatWithCommas(BigDecimal number) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(number);
    }
    @GetMapping("/welcome")
    public ModelAndView welcome(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/other/welcome");
        Map<String, Object> map = new HashMap<String, Object>(2) {
            {
                put("begin", 0);
                put("size",6);
            }
        };
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        if (userToken != null && userToken.getUser() != null) {
            int userDepositoryId = userToken.getUser().getDepositoryId();
            if (userDepositoryId != 0) {
                // 如果用户的depository_id不为0，则根据depository_id过滤公告
                map.put("depositoryId", userDepositoryId);
            }
            // 如果用户的depository_id为0，则不对公告进行厂区过滤
            List<Notice> notices = noticeService.findNoticeByCondition(map);
            mv.addObject("notices", notices);
        }
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("materials", materialService.findMaterialAll());
        mv.addObject("materialTypes", materialTypeService.findMaterialTypeAll());
//        mv.addObject("SABpriceSum", materialService.findSABpriceSum());
//        mv.addObject("ZABpriceSum", materialService.findZABpriceSum());
        DecimalFormat df = new DecimalFormat("#,##0"); // 这会格式化数字为千分位格式，并且始终有两位小数
        BigDecimal sabPriceSum = materialService.findSABpriceSum();
        BigDecimal zabPriceSum = materialService.findZABpriceSum();
        mv.addObject("SABpriceSum", df.format(sabPriceSum));
        mv.addObject("ZABpriceSum", df.format(zabPriceSum));
        mv.addObject("SABcountSum", materialService.findSABcountSum());
        mv.addObject("ZABcountSum", materialService.findZABcountSum());
        // 添加 materials 的总数
        int count = materialService.findCount();
        mv.addObject("count", count);
        return mv;
    }

    @GetMapping("/depository_add")
    public String depository_add() {
        return "pages/other/depository_add";
    }

    @GetMapping("/materialType_add")
    public String materialType_add() {
        return "pages/other/materialType_add";
    }

    @GetMapping("/materialEngin_add")
    public String materialEngin_add() {
        return "pages/other/materialEngin_add";
    }
    @GetMapping("/materialState_add")
    public String materialState_add() {
        return "pages/other/materialState_add";
    }
    @GetMapping("/total_table")
    public String total_table() {
        return "pages/chart/total_table";
    }
    @GetMapping("/sluggish_table")
    public String sluggish_table() {
        return "pages/chart/sluggish_table";
    }
    @GetMapping("/every_type")
    public String every_type() {
        return "pages/chart/every_type";
    }
    @GetMapping("/transfer_table")
    public String transfer_table() {
        return "pages/chart/transfer_table";
    }
    @GetMapping("/rate_add")
    public String rate_add() { return "pages/other/rate_add"; }
    @GetMapping("/productInfo_add")
    public String productInfo_add() {
        return "pages/other/productInfo_add";
    }

    @GetMapping("/dropData_add")
    public String dropData_add() {
        return "pages/other/dropData_add";
    }
    @GetMapping("/saoma")
    public String saoma() {
        return "pages/other/saoma";
    }
    @GetMapping("/once_add")
    public ModelAndView once_add() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/other/once_add");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("materialTypes", materialTypeService.findMaterialTypeAll());
        mv.addObject("materialEngins", materialEnginService.findMaterialEnginAll());
        return mv;
    }
    @GetMapping("/once_table")
    public ModelAndView once_table() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/chart/once_table");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("materialTypes", materialTypeService.findMaterialTypeAll());
        mv.addObject("materialEngins", materialEnginService.findMaterialEnginAll());
        return mv;
    }

    @GetMapping("/material_add")
    public ModelAndView material_add() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/other/material_add");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("materialTypes", materialTypeService.findMaterialTypeAll());
        mv.addObject("materialEngins", materialEnginService.findMaterialEnginAll());
        mv.addObject("materialStates",materialStateService.findMaterialStateAll());
        return mv;
    }

    @GetMapping("/application_in")
    public ModelAndView application_in() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/application/application-in");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("reviewers", userService.findReviewers());
        return mv;
    }

    @GetMapping("/application_out")
    public ModelAndView application_out() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/application/application-out");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("reviewers", userService.findReviewers());
        return mv;
    }

    @GetMapping("/application_transfer")
    public ModelAndView application_transfer() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/application/application-transfer");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("reviewers", userService.findReviewers());
        return mv;
    }

    @GetMapping("/table_in")
    public ModelAndView table_in() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/depository/table-in");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("materialTypes", materialTypeService.findMaterialTypeAll());
        return mv;
    }

    @GetMapping("/table_out")
    public ModelAndView table_out(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/depository/table-out");

        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("materialTypes", materialTypeService.findMaterialTypeAll());

        // 获取用户信息
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        // 确保userToken和user对象不为null
        if (userToken != null && userToken.getUser() != null) {
            int userDepositoryId = userToken.getUser().getDepositoryId();
            // 根据 userDepositoryId 决定使用哪个 categories 列表
            List<Category> categories;
            if (userDepositoryId == 1) {
                categories = categoryService.getSABCategories();
            } else if (userDepositoryId == 2) {
                categories = categoryService.getZABCategories();
            } else {
                categories = categoryService.getAllCategories();
            }
            mv.addObject("cas", categories);
        } else {
            // 处理 userToken 或 user 对象为 null 的情况，你可能需要重定向到登录页面或显示错误消息
        }
        return mv;
    }


    @GetMapping("/table_user")
    public ModelAndView table_user() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/user/table-user");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        return mv;
    }

    @GetMapping("/table_stock")
    public ModelAndView table_stock() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/depository/table-stock");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("materialTypes",materialTypeService.findMaterialTypeAll());
        mv.addObject("materialStates",materialStateService.findMaterialStateAll());
        return mv;
    }

    @GetMapping("/my_task")
    public String my_task() {
        return "pages/application/my-task";
    }

    @GetMapping("/my_apply")
    public String my_apply() {
        return "pages/application/my-apply";
    }

    @GetMapping("/notice_edit")
    public String notice_edit() {
        return "pages/other/notice-edit";
    }

    @GetMapping("/chart_in")
    public String chart_in() {
        return "pages/chart/chart-in";
    }

    @GetMapping("/chart_out")
    public String chart_out() {
        return "pages/chart/chart-out";
    }

    @GetMapping("/chart_stock")
    public String chart_stock() {
        return "pages/chart/chart-stock";
    }

    @GetMapping("/user_password")
    public String user_password() {
        return "pages/user/user-password";
    }

    @GetMapping("/user_add")
    public ModelAndView user_add() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/user/user-add");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        return mv;
    }

    @GetMapping("/user_edit")
    public ModelAndView user_edit(Integer id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/user/user-edit");
        mv.addObject("depositories", depositoryService.findDepositoryAll());
        mv.addObject("user", userService.findUserPById(id));
        return mv;
    }

    @GetMapping("/form_step_look")
    public ModelAndView form_step_look(Integer id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/application/form-step-look");
        if (id != null) {
            mv.addObject("record", depositoryRecordService.findDepositoryRecordById(id));
        } else {
            throw new MyException("缺少必要参数！");
        }
        return mv;
    }

    @GetMapping("/application_review")
    public ModelAndView application_review(Integer id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/application/application-review");
        DepositoryRecordP recordP = depositoryRecordService.findDepositoryRecordById(id);
        mv.addObject("record", recordP);
        mv.addObject("checkers", userService.findUsersByDepositoryId(recordP.getDepositoryId()));
        return mv;
    }

    @GetMapping("/account_look")
    public ModelAndView account_look(Integer id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/user/account-look");
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        mv.addObject("user", userToken.getUser());
        return mv;
    }

    @GetMapping("/user_email")
    public ModelAndView user_email(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/user/user-email");
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        mv.addObject("email", userToken.getUser().getEmail());
        return mv;
    }
}
