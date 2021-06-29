package com.example.community_stack.models;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatDialog implements IDialog {
    public  String id;
    public String dialogPhoto;
    public String dialogName;
    public ArrayList<com.example.community_stack.models.User> users = new ArrayList<>();
    public com.example.community_stack.models.Message lastMessage;
    public int unreadCount;
    public String suggestion_details;
    public String createdBy;
    public long upvotes;


    public long getUpvotes() {
        return upvotes;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSuggestion_details() {
        return suggestion_details;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<com.example.community_stack.models.User> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage lastMessage) {
        this.lastMessage = (com.example.community_stack.models.Message) lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public Map<String, Object> hashMap(){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("dialogName", dialogName);
        hashMap.put("dialogPhoto", dialogPhoto);
        hashMap.put("users",users);
        hashMap.put("lastMessage", lastMessage);
        hashMap.put("unreadCount", unreadCount);
        hashMap.put("suggestion_details", suggestion_details);
        hashMap.put("createdBy", createdBy);
        hashMap.put("upvotes", upvotes);


        return hashMap;
    }
}
