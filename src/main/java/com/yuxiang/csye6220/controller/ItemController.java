package com.yuxiang.csye6220.controller;

import com.yuxiang.csye6220.pojo.ItemDTO;
import com.yuxiang.csye6220.pojo.ItemEntity;
import com.yuxiang.csye6220.pojo.SellerEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ApplicationContext applicationContext;

    private Configuration configuration;

    private SessionFactory sessionFactory;

    @Autowired
    public ItemController(Configuration configuration){
        this.configuration = configuration;
        this.sessionFactory = this.configuration.buildSessionFactory();
    }

    @GetMapping("/all")
    public String handleGet_itemAll(@SessionAttribute(name = "seller", required = false) SellerEntity sellerEntity, Model model){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        String hql = "FROM ItemEntity itemEntity WHERE itemEntity.seller.id = :sellerId";
        try(Session session = sessionFactory.openSession()){
            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("sellerId", sellerEntity.getId());
            List<ItemEntity> items = query.list();
            model.addAttribute("items", items);
        }
        return "item-all";
    }

    @GetMapping("/{itemId}")
    public String handleGet_item(@PathVariable(name = "itemId") int itemId,
                                 @SessionAttribute(name = "seller", required = false)SellerEntity sellerEntity,
                                 Model model
    ){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        String hql = "FROM ItemEntity itemEntity WHERE itemEntity.id = :itemId";
        try(Session session = sessionFactory.openSession()){
            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("itemId", itemId);
            ItemEntity itemEntity = query.getSingleResultOrNull();
            model.addAttribute("itemEntity", itemEntity);
        }
        return "item-page";
    }

    @GetMapping("/modify/{itemId}")
    public String handleGet_itemModify(@PathVariable(name = "itemId") int itemId,
                                       @SessionAttribute(name = "seller", required = false)SellerEntity sellerEntity,
                                       Model model
    ){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        String hql = "FROM ItemEntity itemEntity WHERE itemEntity.id = :itemId";
        try(Session session = sessionFactory.openSession()){
            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("itemId", itemId);
            ItemEntity itemEntity = query.getSingleResultOrNull();
            model.addAttribute("itemEntity", itemEntity);
        }
        return "item-modify";
    }

    @PatchMapping("/modify/{itemId}")
    public String handlePatch_itemModify(@PathVariable(name = "itemId") int itemId,
                                         @SessionAttribute(name = "seller", required = false)SellerEntity sellerEntity,
                                         @ModelAttribute(name = "itemDTO") ItemDTO itemDTO
    ){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        try(Session session = sessionFactory.openSession()){
            String hql = "FROM ItemEntity itemEntity WHERE itemEntity.id = :itemId";
            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("itemId", itemId);
            ItemEntity itemEntity = query.getSingleResultOrNull();
            itemDTO.updateInfoToItemEntity(itemEntity);
            Transaction transaction = session.beginTransaction();
            session.persist(itemEntity);
            transaction.commit();
        }
        return "redirect:/item/" + itemId;
    }

    @GetMapping("/modify/{itemId}/media")
    public String handleGet_itemModifyMedia(@PathVariable(name = "itemId") int itemId,
                                            @SessionAttribute(name = "seller", required = false)SellerEntity sellerEntity,
                                            Model model
    ){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        try(Session session = sessionFactory.openSession()){
            String hql = "FROM ItemEntity itemEntity WHERE itemEntity.id = :itemId";
            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("itemId", itemId);
            ItemEntity itemEntity = query.getSingleResultOrNull();
            List<String> url2medias = itemEntity.getUrl2media();
            model.addAttribute("url2medias", url2medias);
        }
        return "item-modify-media";
    }

    @DeleteMapping("/modify/{itemId}/media")
    public String handleDelete_itemModifyMedia(@PathVariable(name = "itemId") int itemId,
                                               @SessionAttribute(name = "seller", required = false)SellerEntity sellerEntity,
                                               @RequestParam(name = "url2media") String url2media,
                                               Model model
    ){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        try(Session session = sessionFactory.openSession()){
            String hql = "FROM ItemEntity itemEntity WHERE itemEntity.id = :itemId";
            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("itemId", itemId);
            ItemEntity itemEntity = query.getSingleResultOrNull();
            List<String> urls = itemEntity.getUrl2media();
            for(int i = 0; i < urls.size(); i++)
                if(urls.get(i).equals(url2media)){
                    urls.remove(i);
                    break;
                }

            // delete image here

            Transaction transaction = session.beginTransaction();
            session.persist(itemEntity);
            transaction.commit();
            model.addAttribute("url2medias", itemEntity.getUrl2media());
        }
        return "item-modify-media";
    }

    @PostMapping("/modify/{itemId}/media")
    public String handlePost_itemModifyMedia(@PathVariable(name = "itemId") int itemId,
                                             @SessionAttribute(name = "seller", required = false)SellerEntity sellerEntity,
                                             @RequestParam(name = "url2media") String url2media,
                                             Model model
    ){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        try(Session session = sessionFactory.openSession()){
            String hql = "FROM ItemEntity itemEntity WHERE itemEntity.id = :itemId";
            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("itemId", itemId);
            ItemEntity itemEntity = query.getSingleResultOrNull();
            List<String> urls = itemEntity.getUrl2media();
            urls.add(url2media);

            // upload image here

            Transaction transaction = session.beginTransaction();
            session.persist(itemEntity);
            transaction.commit();
            model.addAttribute("url2medias", itemEntity.getUrl2media());
        }
        return "item-modify-media";
    }

    @GetMapping("/newItem")
    public String handleGet_newItem(@SessionAttribute(name = "seller", required = false) SellerEntity sellerEntity){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        return "item-new";
    }

    @PostMapping("/newItem")
    public String handlePost_newItem(
            @SessionAttribute(name = "seller", required = false) SellerEntity sellerEntity,
            @ModelAttribute(name = "itemDTO") ItemDTO itemDTO
    ){
        // check login state
        if(sellerEntity == null)
            return "redirect:/login/seller";

        ItemEntity itemEntity = applicationContext.getBean("itemEntity_prototype", ItemEntity.class);
        itemDTO.updateInfoToItemEntity(itemEntity);
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(itemEntity);
            transaction.commit();
        }
        return "item-new-successful";
    }
}