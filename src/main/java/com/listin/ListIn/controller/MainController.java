package com.listin.ListIn.controller;

import com.listin.ListIn.entity.*;
import com.listin.ListIn.repository.*;
import com.listin.ListIn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CheckListRepository checkListRepository;

    @Autowired
    ItemListRepository itemListRepository;

    @Autowired
    ItemOnCheckListRepository itemOnCheckListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(HttpServletRequest request,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.apiKeyGeneration();
        userRepository.save(user);
        userService.autoLogin(request, username, password);
        return "redirect:/checklists";

    }

    @GetMapping("/checklists")
    public String checklists(Model model) {
        User user = userService.getLoggedUsername();
        model.addAttribute("checklists", checkListRepository.findAllByUser(user));

        return "checklistSearch";
    }

    @GetMapping("/searchChecklist")
    public String searchChecklist(Model model, @RequestParam String search){
        User user = userService.getLoggedUsername();
        model.addAttribute("checklists", checkListRepository.findAllByNameLikeAndUser(search, user.getId()));

        return "checklistSearch";
    }

    @GetMapping("/checklists/{id}")
    public String checklist(@PathVariable Long id, Model model) {
        Optional<CheckList> optionalCheckList = checkListRepository.findById(id);

        if (optionalCheckList.isPresent()) {
            List<ItemOnCheckList> itemsOnChecklist = itemOnCheckListRepository
                                                    .findAllByCheckList(optionalCheckList.get());

            HashSet<String> categories = new HashSet<>();

            for (ItemOnCheckList it : itemsOnChecklist) {
                categories.add(it.getItemList().getCategory().getName());
            }

            model.addAttribute("itemsCategory",categoryMap2(categories, itemsOnChecklist));
            model.addAttribute("checklist", optionalCheckList.get());
        }
        return "checklist";
    }
    @GetMapping("/searchItemOnChecklist")
    public String searchItemOnChecklist(@RequestParam Long checklistId,
                                        @RequestParam String search,
                                        Model model){
        List<ItemOnCheckList> allitems = itemOnCheckListRepository.findAllByCheckList(checklistId, search);
        HashSet<String> categories = new HashSet<>();
        for (ItemOnCheckList it : allitems) {
            categories.add(it.getItemList().getCategory().getName());
        }
        model.addAttribute("itemsCategory", categoryMap2(categories, allitems));
        model.addAttribute("checklist", checkListRepository.findById(checklistId).get());
        return "checklist";

    }

    @GetMapping("/newChecklist")
    public String newChecklist() {
        return "newChecklist";
    }

    @PostMapping("/newChecklist")
    public String postnewChecklist(@RequestParam String name) {
        User user = userService.getLoggedUsername();
        CheckList checkList = new CheckList();
        checkList.setName(name);
        checkList.setUser(user);
        checkListRepository.save(checkList);
        return "redirect:/checklists";
    }

    @GetMapping("/addItems")
    public String addItems(@RequestParam Long checklist, Model model) {
        User user = userService.getLoggedUsername();
        List<ItemList> allitems = itemListRepository.findAllByUser(user);
        HashSet<String> categories = new HashSet<>();
        for (ItemList item: allitems) {
            categories.add(item.getCategory().getName());
        }
        model.addAttribute("itemsCategory", categoryMap(categories, allitems));
        model.addAttribute("checklist", checklist);
        return "items";
    }

    @GetMapping("/searchItems")
    public String searchItems(@RequestParam Long checklistId,
                              @RequestParam String search,
                              Model model) {
        User user = userService.getLoggedUsername();
        List<ItemList> items = itemListRepository.findAllByUserAndNameLike(user.getId(), search);
        HashSet<String> categories = new HashSet<>();
        for (ItemList item: items) {
            categories.add(item.getCategory().getName());
        }
        model.addAttribute("itemsCategory", categoryMap(categories, items));
        model.addAttribute("checklist", checklistId);
        return "items";
    }
    @PostMapping("/addItems")
    public String addItem(@RequestParam Long checklistId,
                          @RequestParam Long itemId,
                          @RequestParam Long quantity){
        Optional<CheckList> optionalCheckList = checkListRepository.findById(checklistId);
        Optional<ItemList> optionalItemList = itemListRepository.findById(itemId);
        Optional<ItemOnCheckList> optionalItemOnCheckList = itemOnCheckListRepository
                                                            .findByCheckListAndItemList
                                                                    (optionalCheckList.get(), optionalItemList.get());
        ItemOnCheckList itemOnCheckList = new ItemOnCheckList();
        if (optionalItemOnCheckList.isPresent()) {
            itemOnCheckList = optionalItemOnCheckList.get();
            itemOnCheckList.setQuantity(itemOnCheckList.getQuantity() + quantity);

        } else {
            itemOnCheckList.setQuantity(quantity);
            itemOnCheckList.setItemList(optionalItemList.get());
            itemOnCheckList.setCheckList(optionalCheckList.get());
        }

        itemOnCheckListRepository.save(itemOnCheckList);
        return "redirect:/addItems?checklist=" + checklistId;
    }

    @GetMapping("/newItem")
    public String newItem(@RequestParam Long checklist, Model model) {
        User user = userService.getLoggedUsername();
        List<Category> categories = categoryRepository.findByUserId(user.getId());
        model.addAttribute("categories", categories);
        model.addAttribute("checklist", checklist);

        return "newItem";
    }

    @PostMapping("/newItem")
    public String addNewItem(@RequestParam Long checklistId,
                             @RequestParam Long category,
                             @RequestParam String name,
                             @RequestParam(defaultValue = "", required = false) String newCategory) {
        User user = userService.getLoggedUsername();
        ItemList item = new ItemList();
        item.setUser(user);
        item.setName(name);
        if (category != 0) {
            item.setCategory(categoryRepository.findById(category).get());
        } else {
            Category categoryNew = new Category();
            categoryNew.setName(newCategory);
            categoryRepository.save(categoryNew);
            item.setCategory(categoryNew);
        }
        item = itemListRepository.save(item);
        ItemOnCheckList itemOnCheckList = new ItemOnCheckList();
        itemOnCheckList.setCheckList(checkListRepository.findById(checklistId).get());
        itemOnCheckList.setItemList(item);
        return "redirect:/addItems?checklist=" + checklistId;

    }

    @GetMapping("/validateItem/{id}")
    public Boolean validateItem(@PathVariable Long id) {
        Optional<ItemOnCheckList> optionalItemOnCheckList = itemOnCheckListRepository.findById(id);
        if (optionalItemOnCheckList.isPresent()) {
            ItemOnCheckList itemOnCheckList = optionalItemOnCheckList.get();
            if (itemOnCheckList.getDone() == null || !itemOnCheckList.getDone()) {
                itemOnCheckList.setDone(true);
            } else {
                itemOnCheckList.setDone(false);
            }
            itemOnCheckListRepository.save(itemOnCheckList);
            return true;
        }
        return false;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = userService.getLoggedUsername();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/changepassw")
    public String modifypsw(@RequestParam String password, Model model) {
        User user = userService.getLoggedUsername();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        model.addAttribute("user", user);
        model.addAttribute("done", true);
        return "profile";
    }

    // http://websystique.com/spring-security/spring-security-4-logout-example/
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }


    public Map<String, List<ItemList>> categoryMap (HashSet<String> categories, List<ItemList> allitems) {
        Map<String, List<ItemList>> mapItems = new HashMap<>();
        for (String category : categories) {
            List<ItemList> itemsCategory = new ArrayList<>();
            for (ItemList item: allitems) {
                if (item.getCategory().getName().equals(category)) {
                    itemsCategory.add(item);
                }
            }
            mapItems.put(category, itemsCategory);
        }
        return mapItems;
    }

    public Map<String, List<ItemOnCheckList>> categoryMap2 (HashSet<String> categories, List<ItemOnCheckList> allitems) {
        Map<String, List<ItemOnCheckList>> mapItems = new HashMap<>();
        for (String category : categories) {
            List<ItemOnCheckList> itemsCategory = new ArrayList<>();
            for (ItemOnCheckList item: allitems) {
                if (item.getItemList().getCategory().getName().equals(category)) {
                    itemsCategory.add(item);
                }
            }
            mapItems.put(category, itemsCategory);
        }
        return mapItems;
    }

}
