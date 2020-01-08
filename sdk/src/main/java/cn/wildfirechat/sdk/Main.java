package cn.wildfirechat.sdk;

import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.pojos.*;
import cn.wildfirechat.proto.ProtoConstants;
import cn.wildfirechat.sdk.model.IMResult;
import cn.wildfirechat.sdk.utilities.AdminHttpUtils;
import cn.wildfirechat.sdk.utilities.RobotHttpUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static cn.wildfirechat.proto.ProtoConstants.SystemSettingType.Group_Max_Member_Count;

public class Main {
    public static void main(String[] args) throws Exception {
        testAdmin();
        testRobot();
//        testChannel();
    }

    static void testAdmin() throws Exception {
        //初始化服务API
        AdminHttpUtils.init("http://localhost:18080", "123456");

        //***********************************************
        //****  用户相关的API
        //***********************************************
        InputOutputUserInfo userInfo = new InputOutputUserInfo();
        userInfo.setUserId("userId1");
        userInfo.setName("user1");
        userInfo.setMobile("13900000000");
        userInfo.setDisplayName("user 1");

        IMResult<OutputCreateUser> resultCreateUser = UserAdmin.createUser(userInfo);
        if (resultCreateUser != null && resultCreateUser.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("Create user " + resultCreateUser.getResult().getName() + " success");
        } else {
            System.out.println("Create user failure");
            System.exit(-1);
        }

        InputCreateRobot createRobot = new InputCreateRobot();
        createRobot.setUserId("robot1");
        createRobot.setName("robot1");
        createRobot.setDisplayName("机器人");
        createRobot.setOwner("userId1");
        createRobot.setSecret("123456");
        createRobot.setCallback("http://127.0.0.1:8883/robot/recvmsg");
        IMResult<OutputCreateRobot> resultCreateRobot = UserAdmin.createRobot(createRobot);
        if (resultCreateRobot != null && resultCreateRobot.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("Create robot " + resultCreateRobot.getResult().getUserId() + " success");
        } else {
            System.out.println("Create robot failure");
            System.exit(-1);
        }

        IMResult<InputOutputUserInfo> resultGetUserInfo1 = UserAdmin.getUserByName(userInfo.getName());
        if (resultGetUserInfo1 != null && resultGetUserInfo1.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if (userInfo.getUserId().equals(resultGetUserInfo1.getResult().getUserId())
                && userInfo.getName().equals(resultGetUserInfo1.getResult().getName())
                && userInfo.getMobile().equals(resultGetUserInfo1.getResult().getMobile())
                && userInfo.getDisplayName().equals(resultGetUserInfo1.getResult().getDisplayName())) {
                System.out.println("get user info success");
            } else {
                System.out.println("get user info by name failure");
                System.exit(-1);
            }
        } else {
            System.out.println("get user info by name failure");
            System.exit(-1);
        }

        IMResult<InputOutputUserInfo> resultGetUserInfo2 = UserAdmin.getUserByMobile(userInfo.getMobile());
        if (resultGetUserInfo2 != null && resultGetUserInfo2.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if (userInfo.getUserId().equals(resultGetUserInfo2.getResult().getUserId())
                && userInfo.getName().equals(resultGetUserInfo2.getResult().getName())
                && userInfo.getMobile().equals(resultGetUserInfo2.getResult().getMobile())
                && userInfo.getDisplayName().equals(resultGetUserInfo2.getResult().getDisplayName())) {
                System.out.println("get user info success");
            } else {
                System.out.println("get user info by mobile failure");
                System.exit(-1);
            }
        } else {
            System.out.println("get user info by mobile failure");
            System.exit(-1);
        }

        IMResult<InputOutputUserInfo> resultGetUserInfo3 = UserAdmin.getUserByUserId(userInfo.getUserId());
        if (resultGetUserInfo3 != null && resultGetUserInfo3.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if (userInfo.getUserId().equals(resultGetUserInfo3.getResult().getUserId())
                && userInfo.getName().equals(resultGetUserInfo3.getResult().getName())
                && userInfo.getMobile().equals(resultGetUserInfo3.getResult().getMobile())
                && userInfo.getDisplayName().equals(resultGetUserInfo3.getResult().getDisplayName())) {
                System.out.println("get user info success");
            } else {
                System.out.println("get user info by userId failure");
                System.exit(-1);
            }
        } else {
            System.out.println("get user info by userId failure");
            System.exit(-1);
        }

        IMResult<OutputGetIMTokenData> resultGetToken = UserAdmin.getUserToken(userInfo.getUserId(), "client111", ProtoConstants.Platform.Platform_Android);
        if (resultGetToken != null && resultGetToken.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("get token success: " + resultGetToken.getResult().getToken());
        } else {
            System.out.println("get user token failure");
            System.exit(-1);
        }

        IMResult<Void> resultVoid =UserAdmin.updateUserBlockStatus(userInfo.getUserId(), 2);
        if (resultVoid != null && resultVoid.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("block user done");
        } else {
            System.out.println("block user failure");
            System.exit(-1);
        }

        IMResult<OutputUserStatus> resultCheckUserStatus = UserAdmin.checkUserBlockStatus(userInfo.getUserId());
        if (resultCheckUserStatus != null && resultCheckUserStatus.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if (resultCheckUserStatus.getResult().getStatus() == 2) {
                System.out.println("check user status success");
            } else {
                System.out.println("user status not correct");
                System.exit(-1);
            }
        } else {
            System.out.println("block user failure");
            System.exit(-1);
        }

        IMResult<OutputUserBlockStatusList> resultBlockStatusList = UserAdmin.getBlockedList();
        if (resultBlockStatusList != null && resultBlockStatusList.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            boolean success = false;
            for (InputOutputUserBlockStatus blockStatus : resultBlockStatusList.getResult().getStatusList()) {
                if (blockStatus.getUserId().equals(userInfo.getUserId()) && blockStatus.getStatus() == 2) {
                    System.out.println("get block list done");
                    success = true;
                    break;
                }
            }
            if (!success) {
                System.out.println("block user status is not expected");
                System.exit(-1);
            }
        } else {
            System.out.println("block user failure");
            System.exit(-1);
        }

        resultVoid =UserAdmin.updateUserBlockStatus(userInfo.getUserId(), 0);
        if (resultVoid != null && resultVoid.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("block user done");
        } else {
            System.out.println("block user failure");
            System.exit(-1);
        }

        resultCheckUserStatus = UserAdmin.checkUserBlockStatus(userInfo.getUserId());
        if (resultCheckUserStatus != null && resultCheckUserStatus.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if (resultCheckUserStatus.getResult().getStatus() == 0) {
                System.out.println("check user status success");
            } else {
                System.out.println("user status not correct");
                System.exit(-1);
            }
        } else {
            System.out.println("block user failure");
            System.exit(-1);
        }

        //***********************************************
        //****  群组相关功能
        //***********************************************
        IMResult<Void> voidIMResult1 = GroupAdmin.dismissGroup("user1", "groupId1", null, null);

        PojoGroupInfo groupInfo = new PojoGroupInfo();
        groupInfo.setTarget_id("groupId1");
        groupInfo.setOwner("user1");
        groupInfo.setName("test_group");
        groupInfo.setExtra("hello extra");
        groupInfo.setPortrait("http://portrait");
        List<PojoGroupMember> members = new ArrayList<>();
        PojoGroupMember member1 = new PojoGroupMember();
        member1.setMember_id(groupInfo.getOwner());
        members.add(member1);

        PojoGroupMember member2 = new PojoGroupMember();
        member2.setMember_id("user2");
        members.add(member2);

        PojoGroupMember member3 = new PojoGroupMember();
        member3.setMember_id("user3");
        members.add(member3);

        IMResult<OutputCreateGroupResult> resultCreateGroup = GroupAdmin.createGroup(groupInfo.getOwner(), groupInfo, members, null, null);
        if (resultCreateGroup != null && resultCreateGroup.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("create group success");
        } else {
            System.out.println("create group failure");
            System.exit(-1);
        }

        IMResult<PojoGroupInfo> resultGetGroupInfo = GroupAdmin.getGroupInfo(groupInfo.getTarget_id());
        if (resultGetGroupInfo != null && resultGetGroupInfo.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if (groupInfo.getExtra().equals(resultGetGroupInfo.getResult().getExtra())
                && groupInfo.getName().equals(resultGetGroupInfo.getResult().getName())
                && groupInfo.getOwner().equals(resultGetGroupInfo.getResult().getOwner())) {
                System.out.println("get group success");
            } else {
                System.out.println("group info is not expected");
                System.exit(-1);
            }
        } else {
            System.out.println("create group failure");
            System.exit(-1);
        }

        IMResult<Void> voidIMResult = GroupAdmin.transferGroup(groupInfo.getOwner(), groupInfo.getTarget_id(), "user2", null, null);
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("transfer success");
        } else {
            System.out.println("create group failure");
            System.exit(-1);
        }

        voidIMResult = GroupAdmin.modifyGroupInfo(groupInfo.getOwner(), groupInfo.getTarget_id(), ProtoConstants.ModifyGroupInfoType.Modify_Group_Name,"HelloWorld", null);
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("transfer success");
        } else {
            System.out.println("create group failure");
            System.exit(-1);
        }


        resultGetGroupInfo = GroupAdmin.getGroupInfo(groupInfo.getTarget_id());
        if (resultGetGroupInfo != null && resultGetGroupInfo.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if ("user2".equals(resultGetGroupInfo.getResult().getOwner())) {
                groupInfo.setOwner("user2");
            } else {
                System.out.println("group info is not expected");
                System.exit(-1);
            }
        } else {
            System.out.println("create group failure");
            System.exit(-1);
        }

        IMResult<OutputGroupMemberList> resultGetMembers = GroupAdmin.getGroupMembers(groupInfo.getTarget_id());
        if (resultGetMembers != null && resultGetMembers.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("get group member success");
        } else {
            System.out.println("create group failure");
            System.exit(-1);
        }

        voidIMResult = GroupAdmin.addGroupMembers("user1", groupInfo.getTarget_id(), Arrays.asList("use4", "user5"), null, null);
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("add group member success");
        } else {
            System.out.println("add group member failure");
            System.exit(-1);
        }

        voidIMResult = GroupAdmin.kickoffGroupMembers("user1", groupInfo.getTarget_id(), Arrays.asList("user3"), null, null);
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("kickoff group member success");
        } else {
            System.out.println("kickoff group member failure");
            System.exit(-1);
        }

        voidIMResult = GroupAdmin.setGroupManager("user1", groupInfo.getTarget_id(), Arrays.asList("user4", "user5"), true, null, null);
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("set group manager success");
        } else {
            System.out.println("set group manager failure");
            System.exit(-1);
        }

        voidIMResult = GroupAdmin.setGroupManager("user1", groupInfo.getTarget_id(), Arrays.asList("user4", "user5"), false, null, null);
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("cancel group manager success");
        } else {
            System.out.println("cancel group manager failure");
            System.exit(-1);
        }


        voidIMResult = GroupAdmin.quitGroup("user4", groupInfo.getTarget_id(), null, null);
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("quit group success");
        } else {
            System.out.println("quit group failure");
            System.exit(-1);
        }

        IMResult<OutputGroupIds> groupIdsIMResult = GroupAdmin.getUserGroups("user1");
        if (groupIdsIMResult != null && groupIdsIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            if (groupIdsIMResult.getResult().getGroupIds().contains(groupInfo.getTarget_id())) {
                System.out.println("get user groups success");
            } else {
                System.out.println("get user groups failure");
                System.exit(-1);
            }
        } else {
            System.out.println("get user groups failure");
            System.exit(-1);
        }


        //***********************************************
        //****  消息相关功能
        //***********************************************
        Conversation conversation = new Conversation();
        conversation.setTarget("user2");
        conversation.setType(ProtoConstants.ConversationType.ConversationType_Private);
        MessagePayload payload = new MessagePayload();
        payload.setType(1);
        payload.setSearchableContent("hello world");

        IMResult<SendMessageResult> resultSendMessage = MessageAdmin.sendMessage("user1", conversation, payload);
        if (resultSendMessage != null && resultSendMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("send message success");
        } else {
            System.out.println("send message failure");
            System.exit(-1);
        }


        voidIMResult = MessageAdmin.recallMessage("user1", resultSendMessage.getResult().getMessageUid());
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("recall message success");
        } else {
            System.out.println("recall message failure");
            System.exit(-1);
        }


        IMResult<BroadMessageResult> resultBroadcastMessage = MessageAdmin.broadcastMessage("user1", 0, payload);
        if (resultBroadcastMessage != null && resultBroadcastMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("broad message success, send message to " + resultBroadcastMessage.getResult().getCount() + " users");
        } else {
            System.out.println("broad message failure");
            System.exit(-1);
        }

        List<String> multicastReceivers = Arrays.asList("user2", "user3", "user4");
        IMResult<MultiMessageResult> resultMulticastMessage = MessageAdmin.multicastMessage("user1", multicastReceivers, 0, payload);
        if (resultMulticastMessage != null && resultMulticastMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("multi message success, messageid is " + resultMulticastMessage.getResult().getMessageUid());
        } else {
            System.out.println("multi message failure");
            System.exit(-1);
        }

        IMResult<SystemSettingPojo> resultGetSystemSetting  =  GeneralAdmin.getSystemSetting(Group_Max_Member_Count);
        if (resultGetSystemSetting != null && resultGetSystemSetting.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("success");
        } else {
            System.out.println("get system setting failure");
            System.exit(-1);
        }

        IMResult<Void> resultSetSystemSetting = GeneralAdmin.setSystemSetting(Group_Max_Member_Count, "2000", "最大群人数为2000");
        if (resultSetSystemSetting != null && resultSetSystemSetting.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("success");
        } else {
            System.out.println("get system setting failure");
            System.exit(-1);
        }

        resultGetSystemSetting  =  GeneralAdmin.getSystemSetting(Group_Max_Member_Count);
        if (resultGetSystemSetting != null && resultGetSystemSetting.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS && resultGetSystemSetting.getResult().value.equals("2000")) {
            System.out.println("success");
        } else {
            System.out.println("get system setting failure");
            System.exit(-1);
        }

        InputCreateChannel inputCreateChannel = new InputCreateChannel();
        inputCreateChannel.setName("MyChannel");
        inputCreateChannel.setOwner("user1");
        IMResult<OutputCreateChannel> resultCreateChannel = GeneralAdmin.createChannel(inputCreateChannel);
        if (resultCreateChannel != null && resultCreateChannel.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("success");
        } else {
            System.out.println("create channel failure");
            System.exit(-1);
        }


        //***********************************************
        //****  好友相关功能
        //***********************************************
        //先创建2个用户
        userInfo = new InputOutputUserInfo();
        userInfo.setUserId("ff1");
        userInfo.setName("ff1");
        userInfo.setMobile("13800000000");
        userInfo.setDisplayName("ff1");

        resultCreateUser = UserAdmin.createUser(userInfo);
        if (resultCreateUser != null && resultCreateUser.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("Create user " + resultCreateUser.getResult().getName() + " success");
        } else {
            System.out.println("Create user failure");
            System.exit(-1);
        }

        userInfo = new InputOutputUserInfo();
        userInfo.setUserId("ff2");
        userInfo.setName("ff2");
        userInfo.setMobile("13800000001");
        userInfo.setDisplayName("ff2");

        resultCreateUser = UserAdmin.createUser(userInfo);
        if (resultCreateUser != null && resultCreateUser.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("Create user " + resultCreateUser.getResult().getName() + " success");
        } else {
            System.out.println("Create user failure");
            System.exit(-1);
        }

        IMResult<Void> updateFriendStatusResult = FriendAdmin.updateFriendStatus("ff1", "ff2", 1);
        if (updateFriendStatusResult != null && updateFriendStatusResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("update friend status success");
        } else {
            System.out.println("update friend status failure");
            System.exit(-1);
        }

        IMResult<OutputStringList> resultGetFriendList = FriendAdmin.getFriendStatusList("ff1", 1);
        if (resultGetFriendList != null && resultGetFriendList.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS && resultGetFriendList.getResult().getList().contains("ff2")) {
            System.out.println("get friend status success");
        } else {
            System.out.println("get friend status failure");
            System.exit(-1);
        }

        String alias = "hello" + System.currentTimeMillis();
        IMResult<Void> updateFriendAlias = FriendAdmin.updateFriendAlias("ff1", "ff2", alias);
        if (updateFriendAlias != null && updateFriendAlias.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("update friend status success");
        } else {
            System.out.println("update friend status failure");
            System.exit(-1);
        }

        IMResult<OutputGetAlias> getFriendAlias = FriendAdmin.getFriendAlias("ff1", "ff2");
        if (getFriendAlias != null && getFriendAlias.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS && getFriendAlias.getResult().getAlias().equals(alias)) {
            System.out.println("update friend status success");
        } else {
            System.out.println("update friend status failure");
            System.exit(-1);
        }


        voidIMResult = UserAdmin.destroyUser("user11");
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("destroy user success");
        } else {
            System.out.println("destroy user failure");
            System.exit(-1);
        }
    }

    static void testRobot() throws Exception {
        //初始化机器人API
        RobotHttpUtils.init("http://localhost", "robot1", "123456");
        //***********************************************
        //****  机器人API
        //***********************************************
        Conversation conversation = new Conversation();
        conversation.setTarget("user2");
        conversation.setType(ProtoConstants.ConversationType.ConversationType_Private);
        MessagePayload payload = new MessagePayload();
        payload.setType(1);
        payload.setSearchableContent("hello world");

        IMResult<SendMessageResult> resultRobotSendMessage = RobotService.sendMessage("robot1", conversation, payload);
        if (resultRobotSendMessage != null && resultRobotSendMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("robot send message success");
        } else {
            System.out.println("robot send message failure");
            System.exit(-1);
        }

        IMResult<InputOutputUserInfo> resultRobotGetUserInfo = RobotService.getUserInfo("userId1");
        if (resultRobotGetUserInfo != null && resultRobotGetUserInfo.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("robot get user info success");
        } else {
            System.out.println("robot get user info by userId failure");
            System.exit(-1);
        }
    }

    //***测试频道API功能，仅专业版支持***
    static void testChannel() throws Exception {
        //初始化服务API
        AdminHttpUtils.init("http://localhost:18080", "123456");

        //先创建3个用户
        InputOutputUserInfo userInfo = new InputOutputUserInfo();
        userInfo.setUserId("userId1");
        userInfo.setName("user1");
        userInfo.setMobile("13900000000");
        userInfo.setDisplayName("user 1");

        IMResult<OutputCreateUser> resultCreateUser = UserAdmin.createUser(userInfo);
        if (resultCreateUser != null && resultCreateUser.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("Create user " + resultCreateUser.getResult().getName() + " success");
        } else {
            System.out.println("Create user failure");
            System.exit(-1);
        }

        userInfo = new InputOutputUserInfo();
        userInfo.setUserId("userId2");
        userInfo.setName("user2");
        userInfo.setMobile("13900000002");
        userInfo.setDisplayName("user 2");

        resultCreateUser = UserAdmin.createUser(userInfo);
        if (resultCreateUser != null && resultCreateUser.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("Create user " + resultCreateUser.getResult().getName() + " success");
        } else {
            System.out.println("Create user failure");
            System.exit(-1);
        }

        userInfo = new InputOutputUserInfo();
        userInfo.setUserId("userId3");
        userInfo.setName("user3");
        userInfo.setMobile("13900000003");
        userInfo.setDisplayName("user 3");

        resultCreateUser = UserAdmin.createUser(userInfo);
        if (resultCreateUser != null && resultCreateUser.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("Create user " + resultCreateUser.getResult().getName() + " success");
        } else {
            System.out.println("Create user failure");
            System.exit(-1);
        }

        //1. 先使用admin api创建频道
        InputCreateChannel inputCreateChannel = new InputCreateChannel();
        inputCreateChannel.setName("testChannel");
        inputCreateChannel.setOwner("userId1");
        IMResult<OutputCreateChannel> resultCreateChannel = GeneralAdmin.createChannel(inputCreateChannel);
        if (resultCreateChannel != null && resultCreateChannel.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("create channel success");
        } else {
            System.out.println("create channel failure");
            System.exit(-1);
        }

        //2. 初始化api
        ChannelServiceApi channelServiceApi = new ChannelServiceApi("http://localhost", resultCreateChannel.getResult().getTargetId(), resultCreateChannel.getResult().getSecret());

        //3. 测试channel api功能
        IMResult<Void> resultVoid = channelServiceApi.subscribe("userId2");
        if (resultVoid != null && resultVoid.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("subscribe success");
        } else {
            System.out.println("subscribe failure");
            System.exit(-1);
        }

        resultVoid = channelServiceApi.subscribe("userId3");
        if (resultVoid != null && resultVoid.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("subscribe done");
        } else {
            System.out.println("subscribe failure");
            System.exit(-1);
        }

        IMResult<OutputStringList> resultStringList = channelServiceApi.getSubscriberList();
        if (resultStringList != null && resultStringList.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS && resultStringList.getResult().getList().contains("userId2") && resultStringList.getResult().getList().contains("userId3")) {
            System.out.println("get subscriber done");
        } else {
            System.out.println("get subscriber failure");
            System.exit(-1);
        }

        resultVoid = channelServiceApi.unsubscribe("userId2");
        if (resultVoid != null && resultVoid.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("unsubscriber done");
        } else {
            System.out.println("unsubscriber failure");
            System.exit(-1);
        }

        resultStringList = channelServiceApi.getSubscriberList();
        if (resultStringList != null && resultStringList.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS && resultStringList.getResult().getList().contains("userId3") && !resultStringList.getResult().getList().contains("userId2")) {
            System.out.println("get subscriber done");
        } else {
            System.out.println("get subscriber failure");
            System.exit(-1);
        }

        IMResult<InputOutputUserInfo> resultGetUserInfo1 = channelServiceApi.getUserInfo("userId3");
        if (resultGetUserInfo1 != null && resultGetUserInfo1.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("get user info success");
        } else {
            System.out.println("get user info failure");
            System.exit(-1);
        }


        MessagePayload payload = new MessagePayload();
        payload.setType(1);
        payload.setSearchableContent("hello world");

        IMResult<SendMessageResult> resultSendMessage = channelServiceApi.sendMessage(0, null,payload);
        if (resultSendMessage != null && resultSendMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("send message to all the subscriber success");
        } else {
            System.out.println("send message to all the subscriber  failure");
            System.exit(-1);
        }

        payload.setSearchableContent("hello to user2");

        resultSendMessage = channelServiceApi.sendMessage(0, Arrays.asList("userId2"),payload);
        if (resultSendMessage != null && resultSendMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("send message to user2 success");
        } else {
            System.out.println("send message to user2 failure");
            System.exit(-1);
        }

        IMResult<Void> voidIMResult = channelServiceApi.modifyChannelInfo(ProtoConstants.ModifyChannelInfoType.Modify_Channel_Desc, "this is a test channel, update at:" + new Date().toString());
        if (voidIMResult != null && voidIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("modify channel profile success");
        } else {
            System.out.println("modify channel profile failure");
            System.exit(-1);
        }

        IMResult<OutputGetChannelInfo> outputGetChannelInfoIMResult = channelServiceApi.getChannelInfo();
        if (outputGetChannelInfoIMResult != null && outputGetChannelInfoIMResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            System.out.println("destroy user success");
        } else {
            System.out.println("destroy user failure");
            System.exit(-1);
        }
    }
}
