package com.listin.ListIn.controller;

import com.listin.ListIn.entity.*;
import com.listin.ListIn.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ApiController {


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


    /**
     * OK
     * @param apiKey
     * @return
     */
    @GetMapping("/api/{apiKey}/checklist/")
    private List<CheckList> getChecklists(@PathVariable String apiKey) {

        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);

        return optionalUser.map(user -> checkListRepository.findAllByUser(user)).orElse(new ArrayList<>());

    }

    /**
     * OK
     * @param name
     * @param apiKey
     * @return
     */
    @GetMapping("/api/{apiKey}/checklist/name/{name}")
    private List<CheckList> getChecklistByName(@PathVariable String name, @PathVariable String apiKey) {

        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);

        return optionalUser.map(user -> checkListRepository.findAllByUserAndName(user, name)).orElse(new ArrayList<>());

    }

    /**
     * OK
     * @param id
     * @param apiKey
     * @return
     */
    @GetMapping("/api/{apiKey}/checklist/{id}")
    private CheckList getChecklistById(@PathVariable Long id, @PathVariable String apiKey) {

        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);

        Optional<CheckList> optionalCheckList = checkListRepository.findById(id);

        return optionalCheckList.orElse(null);

    }

    /**
     * OK
     * @param apiKey
     * @param checkList
     * @return
     */
    @PostMapping("api/{apiKey}/checklist")
    private CheckList postChecklist(@PathVariable String apiKey, @RequestBody CheckList checkList) {
        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);

        if (optionalUser.isPresent()) {
            checkList.setUser(optionalUser.get());
            return checkListRepository.save(checkList);
        }

        return null;
    }

    /**
     * OK
     * @param apiKey
     * @param checklistId
     * @param itemId
     * @param qty
     * @return
     */
    @PutMapping("api/{apiKey}/checklist/{checklistId}/{itemId}/{qty}")
    private ItemOnCheckList itemToChecklist(@PathVariable String apiKey,
                                            @PathVariable Long checklistId,
                                            @PathVariable Long itemId,
                                            @PathVariable Long qty) {

        Optional<CheckList> optionalCheckList = checkListRepository.findById(checklistId);
        Optional<ItemList> optionalItemList = itemListRepository.findById(itemId);
        if (optionalCheckList.isPresent() && optionalItemList.isPresent()) {
            ItemOnCheckList itemOnCheckList = new ItemOnCheckList();
            itemOnCheckList.setCheckList(optionalCheckList.get());
            itemOnCheckList.setItemList(optionalItemList.get());
            itemOnCheckList.setQuantity(qty);
            return itemOnCheckListRepository.save(itemOnCheckList);
        }
        return null;
    }

    /**
     *OK
     * @param apiKey
     * @param checklistId
     * @param item
     * @param qty
     * @return
     */
    @PutMapping("api/{apiKey}/checklist/{checklistId}/newitem/{qty}")
    private ItemOnCheckList newItemToChecklist(@PathVariable String apiKey,
                                            @PathVariable Long checklistId,
                                            @RequestBody ItemList item,
                                            @PathVariable Long qty) {
        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            item.setUser(user);
        }
        Optional<CheckList> optionalCheckList = checkListRepository.findById(checklistId);
        ItemList itemList = itemListRepository.save(item);
        if (optionalCheckList.isPresent()) {
            ItemOnCheckList itemOnCheckList = new ItemOnCheckList();
            itemOnCheckList.setCheckList(optionalCheckList.get());
            itemOnCheckList.setItemList(itemList);
            itemOnCheckList.setQuantity(qty);
            return itemOnCheckListRepository.save(itemOnCheckList);
        }
        return null;
    }

    /**
     * OK
     * @param apiKey
     * @param id
     * @return
     */
    @DeleteMapping("api/{apiKey}/checklist/{id}")
    private boolean deleteChecklist(@PathVariable String apiKey, @PathVariable Long id) {
        checkListRepository.deleteById(id);
        return true;
    }

    /**
     * OK
     * @param apiKey
     * @param name
     * @return
     */
    @GetMapping("api/{apiKey}/items/name/{name}")
    private List<ItemList> finditemsbykeyword(@PathVariable String apiKey, @PathVariable String name) {
        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);

        if (optionalUser.isPresent()) {
            return itemListRepository.findAllByUserAndNameLike(optionalUser.get().getId(), name);

        }
        return new ArrayList<>();
    }

    /**
     *OK
     * @param apiKey
     * @param id
     * @return
     */
    @GetMapping("api/{apiKey}/items/{id}")
    private ItemList finditembyId(@PathVariable String apiKey, @PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);

        if (optionalUser.isPresent()) {

            if (itemListRepository.findById(id).isPresent()) {
                return itemListRepository.findById(id).get();
            }
        }
        return null;
    }

    /**
     *OK
     * @param apiKey
     * @param item
     * @return
     */
    @PostMapping("api/{apiKey}/items")
    private ItemList createItem(@PathVariable String apiKey, @RequestBody ItemList item) {
        Optional<User> optionalUser = userRepository.findByApiKey(apiKey);

        if (optionalUser.isPresent()) {
            item.setUser(optionalUser.get());
            return itemListRepository.save(item);
        }

        return null;
    }

    /**
     *OK
     * @param apiKey
     * @param idItem
     * @param idCategory
     * @return
     */
    @PutMapping("api/{apiKey}/items/{idItem}/{idCategory}")
    private ItemList categoryToItem(@PathVariable String apiKey,
                                    @PathVariable Long idItem,
                                    @PathVariable Long idCategory) {

        Optional<ItemList> optionalItemList = itemListRepository.findById(idItem);
        Optional<Category> optionalCategory = categoryRepository.findById(idCategory);

        if (optionalItemList.isPresent() && optionalCategory.isPresent()) {
            ItemList itemList = optionalItemList.get();
            itemList.setCategory(optionalCategory.get());
            return itemListRepository.save(itemList);
        }
        return null;
    }

    /**
     *OK
     * @param apiKey
     * @param idItem
     * @param category
     * @return
     */
    @PutMapping("api/{apiKey}/items/{idItem}")
    private ItemList newCategoryToItem(@PathVariable String apiKey,
                                    @PathVariable Long idItem,
                                    @RequestBody Category category) {

        Optional<ItemList> optionalItemList = itemListRepository.findById(idItem);
        Category newCategory = categoryRepository.save(category);

        if (optionalItemList.isPresent() ) {
            ItemList itemList = optionalItemList.get();
            itemList.setCategory(newCategory);
            return itemListRepository.save(itemList);
        }
        return null;
    }

    /**
     *OK
     * @param apiKey
     * @param id
     * @return
     */
    @DeleteMapping("api/{apiKey}/items/{id}")
    private boolean deleteItem(@PathVariable String apiKey, @PathVariable Long id) {
        itemListRepository.deleteById(id);
        return true;
    }



}
