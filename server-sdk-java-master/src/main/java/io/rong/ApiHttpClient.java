package io.rong;

import io.rong.models.ChatroomInfo;
import io.rong.models.FormatType;
import io.rong.models.GroupInfo;
import io.rong.models.Message;
import io.rong.models.SdkHttpResult;
import io.rong.util.HttpUtil;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;

public class ApiHttpClient {

    private static final String RONGCLOUDURI = "http://api.cn.ronghub.com";

    private static final String UTF8 = "UTF-8";

    // get token
    public static SdkHttpResult getToken(String appKey, String appSecret,
                                         String userId, String userName, String portraitUri,
                                         FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil
                .CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
                        + "/user/getToken." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&name=").append(URLEncoder.encode(userName==null?"":userName, UTF8));
        sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri==null?"":portraitUri, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // check online
    public static SdkHttpResult checkOnline(String appKey, String appSecret,
                                            String userId, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/user/checkOnline." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // refresh user info
    public static SdkHttpResult refreshUser(String appKey, String appSecret,
                                            String userId, String userName, String portraitUri,
                                            FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/user/refresh." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        if (userName != null) {
            sb.append("&name=").append(URLEncoder.encode(userName, UTF8));
        }
        if (portraitUri != null) {
            sb.append("&portraitUri=").append(
                    URLEncoder.encode(portraitUri, UTF8));
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // lock user
    public static SdkHttpResult blockUser(String appKey, String appSecret,
                                          String userId, int minute, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/user/block." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&minute=").append(
                URLEncoder.encode(String.valueOf(minute), UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // unlock user
    public static SdkHttpResult unblockUser(String appKey, String appSecret,
                                            String userId, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/user/unblock." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // get lock user
    public static SdkHttpResult queryBlockUsers(String appKey,
                                                String appSecret, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/user/block/query." + format.toString());

        return HttpUtil.returnResult(conn);
    }

    // add user to black meal
    public static SdkHttpResult blackUser(String appKey, String appSecret,
                                          String userId, List<String> blackUserIds, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/user/blacklist/add." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        if (blackUserIds != null) {
            for (String blackId : blackUserIds) {
                sb.append("&blackUserId=").append(
                        URLEncoder.encode(blackId, UTF8));
            }
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // remove user from black meal
    public static SdkHttpResult unblackUser(String appKey, String appSecret,
                                            String userId, List<String> blackUserIds, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/user/blacklist/remove." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        if (blackUserIds != null) {
            for (String blackId : blackUserIds) {
                sb.append("&blackUserId=").append(
                        URLEncoder.encode(blackId, UTF8));
            }
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // get black meal
    public static SdkHttpResult QueryblackUser(String appKey, String appSecret,
                                               String userId, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/user/blacklist/query." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // create group
    public static SdkHttpResult createGroup(String appKey, String appSecret,
                                            List<String> userIds, String groupId, String groupName,
                                            FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/group/create." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
        sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));
        if (userIds != null) {
            for (String id : userIds) {
                sb.append("&userId=").append(URLEncoder.encode(id, UTF8));
            }
        }
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // join group
    public static SdkHttpResult joinGroup(String appKey, String appSecret,
                                          String userId, String groupId, String groupName, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/group/join." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
        sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // bash join group
    public static SdkHttpResult joinGroupBatch(String appKey, String appSecret,
                                               List<String> userIds, String groupId, String groupName,
                                               FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/group/join." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
        sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));
        if (userIds != null) {
            for (String id : userIds) {
                sb.append("&userId=").append(URLEncoder.encode(id, UTF8));
            }
        }
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // tui chu group
    public static SdkHttpResult quitGroup(String appKey, String appSecret,
                                          String userId, String groupId, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/group/quit." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // bash tui chu group
    public static SdkHttpResult quitGroupBatch(String appKey, String appSecret,
                                               List<String> userIds, String groupId, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/group/quit." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
        if (userIds != null) {
            for (String id : userIds) {
                sb.append("&userId=").append(URLEncoder.encode(id, UTF8));
            }
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // jie san group
    public static SdkHttpResult dismissGroup(String appKey, String appSecret,
                                             String userId, String groupId, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil
                .CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
                        + "/group/dismiss." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // sync usergroup info
    public static SdkHttpResult syncGroup(String appKey, String appSecret,
                                          String userId, List<GroupInfo> groups, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret, RONGCLOUDURI + "/group/sync." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        if (groups != null) {
            for (GroupInfo info : groups) {
                if (info != null) {
                    sb.append(
                            String.format("&group[%s]=",
                                    URLEncoder.encode(info.getId(), UTF8)))
                            .append(URLEncoder.encode(info.getName(), UTF8));
                }
            }
        }
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // refresh group info
    public static SdkHttpResult refreshGroupInfo(String appKey,
                                                 String appSecret, String groupId, String groupName,
                                                 FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil
                .CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
                        + "/group/refresh." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
        sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // refresh group info
    public static SdkHttpResult refreshGroupInfo(String appKey,
                                                 String appSecret, GroupInfo group, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil
                .CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
                        + "/group/refresh." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(URLEncoder.encode(group.getId(), UTF8));
        sb.append("&groupName=").append(
                URLEncoder.encode(group.getName(), UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // send msg
    public static SdkHttpResult publishMessage(String appKey, String appSecret,
                                               String fromUserId, List<String> toUserIds, Message msg,
                                               FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/message/private/publish." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
        if (toUserIds != null) {
            for (int i = 0; i < toUserIds.size(); i++) {
                sb.append("&toUserId=").append(
                        URLEncoder.encode(toUserIds.get(i), UTF8));
            }
        }
        sb.append("&objectName=")
                .append(URLEncoder.encode(msg.getType(), UTF8));
        sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // send msg
    public static SdkHttpResult publishMessage(String appKey, String appSecret,
                                               String fromUserId, List<String> toUserIds, Message msg,
                                               String pushContent, String pushData, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/message/publish." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
        if (toUserIds != null) {
            for (int i = 0; i < toUserIds.size(); i++) {
                sb.append("&toUserId=").append(
                        URLEncoder.encode(toUserIds.get(i), UTF8));
            }
        }
        sb.append("&objectName=")
                .append(URLEncoder.encode(msg.getType(), UTF8));
        sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

        if (pushContent != null) {
            sb.append("&pushContent=").append(
                    URLEncoder.encode(pushContent==null?"":pushContent, UTF8));
        }

        if (pushData != null) {
            sb.append("&pushData=").append(URLEncoder.encode(pushData==null?"":pushData, UTF8));
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // send system msg
    public static SdkHttpResult publishSystemMessage(String appKey,
                                                     String appSecret, String fromUserId, List<String> toUserIds,
                                                     Message msg, String pushContent, String pushData, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/message/system/publish." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
        if (toUserIds != null) {
            for (int i = 0; i < toUserIds.size(); i++) {
                sb.append("&toUserId=").append(
                        URLEncoder.encode(toUserIds.get(i), UTF8));
            }
        }
        sb.append("&objectName=")
                .append(URLEncoder.encode(msg.getType(), UTF8));
        sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

        if (pushContent != null) {
            sb.append("&pushContent=").append(
                    URLEncoder.encode(pushContent==null?"":pushContent, UTF8));
        }

        if (pushData != null) {
            sb.append("&pushData=").append(URLEncoder.encode(pushData==null?"":pushData, UTF8));
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // send group msg
    public static SdkHttpResult publishGroupMessage(String appKey,
                                                    String appSecret, String fromUserId, List<String> toGroupIds,
                                                    Message msg, String pushContent, String pushData, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/message/group/publish." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
        if (toGroupIds != null) {
            for (int i = 0; i < toGroupIds.size(); i++) {
                sb.append("&toGroupId=").append(
                        URLEncoder.encode(toGroupIds.get(i), UTF8));
            }
        }
        sb.append("&objectName=")
                .append(URLEncoder.encode(msg.getType(), UTF8));
        sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

        if (pushContent != null) {
            sb.append("&pushContent=").append(
                    URLEncoder.encode(pushContent==null?"":pushContent, UTF8));
        }

        if (pushData != null) {
            sb.append("&pushData=").append(URLEncoder.encode(pushData==null?"":pushData, UTF8));
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // send chat room msg
    public static SdkHttpResult publishChatroomMessage(String appKey,
                                                       String appSecret, String fromUserId, List<String> toChatroomIds,
                                                       Message msg, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil
                .CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
                        + "/message/chatroom/publish." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
        if (toChatroomIds != null) {
            for (int i = 0; i < toChatroomIds.size(); i++) {
                sb.append("&toChatroomId=").append(
                        URLEncoder.encode(toChatroomIds.get(i), UTF8));
            }
        }
        sb.append("&objectName=")
                .append(URLEncoder.encode(msg.getType(), UTF8));
        sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    public static SdkHttpResult broadcastMessage(String appKey, String appSecret,
                                                 String fromUserId, Message msg,String pushContent, String pushData, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/message/broadcast." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
        sb.append("&objectName=")
                .append(URLEncoder.encode(msg.getType(), UTF8));
        sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));
        if (pushContent != null) {
            sb.append("&pushContent=").append(
                    URLEncoder.encode(pushContent==null?"":pushContent, UTF8));
        }

        if (pushData != null) {
            sb.append("&pushData=").append(URLEncoder.encode(pushData==null?"":pushData, UTF8));
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // create chat room
    public static SdkHttpResult createChatroom(String appKey, String appSecret,
                                               List<ChatroomInfo> chatrooms, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/chatroom/create." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("1=1");
        if (chatrooms != null) {
            for (ChatroomInfo info : chatrooms) {
                if (info != null) {
                    sb.append(
                            String.format("&chatroom[%s]=",
                                    URLEncoder.encode(info.getId(), UTF8)))
                            .append(URLEncoder.encode(info.getName(), UTF8));
                }
            }
        }
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // destroy chat room
    public static SdkHttpResult destroyChatroom(String appKey,
                                                String appSecret, List<String> chatroomIds, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/chatroom/destroy." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("1=1");
        if (chatroomIds != null) {
            for (String id : chatroomIds) {
                sb.append("&chatroomId=").append(URLEncoder.encode(id, UTF8));
            }
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // query chat room info
    public static SdkHttpResult queryChatroom(String appKey, String appSecret,
                                              List<String> chatroomIds, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/chatroom/query." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("1=1");
        if (chatroomIds != null) {
            for (String id : chatroomIds) {
                sb.append("&chatroomId=").append(URLEncoder.encode(id, UTF8));
            }
        }

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // get history info download address.
    public static SdkHttpResult getMessageHistoryUrl(String appKey,
                                                     String appSecret, String date, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/message/history." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("date=").append(URLEncoder.encode(date, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    // delete history info
    public static SdkHttpResult deleteMessageHistory(String appKey,
                                                     String appSecret, String date, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/message/history/delete." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("date=").append(URLEncoder.encode(date, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }
    // get members in group
    public static SdkHttpResult queryGroupUserList(String appKey,
                                                   String appSecret, String groupId, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/group/user/query." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(
                URLEncoder.encode(groupId == null ? "" : groupId, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    public static SdkHttpResult groupUserGagAdd(String appKey,
                                                String appSecret, String groupId, String userId, long minute,
                                                FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/group/user/gag/add." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(
                URLEncoder.encode(groupId == null ? "" : groupId, UTF8));
        sb.append("userId=").append(
                URLEncoder.encode(userId == null ? "" : userId, UTF8));
        sb.append("minute=").append(
                URLEncoder.encode(String.valueOf(minute), UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    public static SdkHttpResult groupUserGagRollback(String appKey,
                                                     String appSecret, String groupId, String userId, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/group/user/gag/rollback." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(
                URLEncoder.encode(groupId == null ? "" : groupId, UTF8));
        sb.append("userId=").append(
                URLEncoder.encode(userId == null ? "" : userId, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    public static SdkHttpResult groupUserGagList(String appKey,
                                                 String appSecret, String groupId, FormatType format)
            throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/group/user/gag/list." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("groupId=").append(
                URLEncoder.encode(groupId == null ? "" : groupId, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    public static SdkHttpResult wordFilterAdd(String appKey, String appSecret,
                                              String word, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/wordfilter/add." + format.toString());

        if (word == null || word.length() == 0) {
            throw new Exception("word is not null or empty.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("word=").append(
                URLEncoder.encode(word == null ? "" : word, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    public static SdkHttpResult wordFilterDelete(String appKey,
                                                 String appSecret, String word, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/wordfilter/delete." + format.toString());

        if (word == null || word.length() == 0) {
            throw new Exception("word is not null or empty.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("word=").append(
                URLEncoder.encode(word == null ? "" : word, UTF8));

        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }

    public static SdkHttpResult wordFilterList(String appKey, String appSecret,
                                               FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
                appSecret,
                RONGCLOUDURI + "/wordfilter/delete." + format.toString());
        StringBuilder sb = new StringBuilder();
        sb.append("1=1");
        HttpUtil.setBodyParameter(sb, conn);
        return HttpUtil.returnResult(conn);
    }
}
