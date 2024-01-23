package com.example.animaland.tool;

import static com.example.animaland.School.Language.ENGLISH;
import static com.example.animaland.School.Language.FRENCH;
import static com.example.animaland.School.Language.JAPANESE;
import static com.example.animaland.School.Language.OTHERS;
import static com.example.animaland.tool.User.roomId;

import android.util.Log;

import com.example.animaland.R;
import com.example.animaland.School.Language;
import com.example.animaland.School.classroom;
import com.example.animaland.School.instructor;
import com.example.animaland.oral.News;
import com.example.animaland.oral.oralUser;
import com.example.animaland.oral.oralroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    User user = new User();
    Utils utils = new Utils();
    //创建地底房间
    public void createUndergroundRoom(String name,String password) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//包含第一个人进房间的信息
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();

        String id;
        ResultSet resultSet = null;
        do {
            id = utils.undergroundId();//不一样！！！
            String sql = "select * from selfRoom where selfRoomId=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            resultSet = pst.executeQuery();
        } while (resultSet.next());//当数据库已经存在这个id，就继续随机生成新的id

        //对selfRoom表：id随机生成 name：输入获取（2-10） notice:进去修改（null） password:有和无 memberNumber:1
        String sql1 = "insert into selfRoom (selfRoomId,name,password,memberNumber) values (?,?,?,?)";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1, id);
        pst1.setString(2, name);
        pst1.setString(3, password);
        pst1.setInt(4, 1);//成员人数默认为1
        pst1.executeUpdate();

        String sql2 = "update users set selfRoomId=? where phoneNumber=?";//在用户中添加房间id
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setString(1, id);
        pst2.setString(2, User.tel);//获取手机号
        pst2.executeUpdate();

        String sql3 = "update selfRoom set seat1=? where selfRoomId=?";//在seat1中加入用户id
        PreparedStatement pst3 = con.prepareStatement(sql3);
        pst3.setString(1, User.tel);
        Log.i("test",User.tel);
        System.out.println(User.tel);
        pst3.setString(2,id);
        pst3.executeUpdate();
        user.setSeat(1);
        user.setRoomId(id);

        String sql4 = "update selfRoom set master=? where selfRoomId=?";//在master中加入用户id
        PreparedStatement pst4 = con.prepareStatement(sql4);
        pst4.setString(1, User.tel);
        pst4.setString(2,id);
        pst4.executeUpdate();
        user.isMaster = true;//设为房主

        pst1.close();
        pst2.close();
        pst3.close();
        pst4.close();
    }
    //创建树屋房间
    public void createTreeRoom(String name,String password) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//包含第一个人进房间的信息
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();

        String id;
        ResultSet resultSet = null;
        do {
            id = utils.treeId();//不一样！！！
            String sql = "select * from selfRoom where selfRoomId=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            resultSet = pst.executeQuery();
        } while (resultSet.next());//当数据库已经存在这个id，就继续随机生成新的id

        //对selfRoom表：id随机生成 name：输入获取（2-10） notice:进去修改（null） password:有和无 memberNumber:1
        String sql1 = "insert into selfRoom (selfRoomId,name,password,memberNumber) values (?,?,?,?)";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1, id);
        pst1.setString(2, name);
        pst1.setString(3, password);
        pst1.setInt(4, 1);//成员人数默认为1
        pst1.executeUpdate();

        String sql2 = "update users set selfRoomId=? where phoneNumber=?";//在用户中添加房间id
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setString(1, id);
        pst2.setString(2, User.tel);//获取手机号
        pst2.executeUpdate();

        String sql3 = "update selfRoom set seat1=? where selfRoomId=?";//在seat1中加入用户id
        PreparedStatement pst3 = con.prepareStatement(sql3);
        pst3.setString(1, User.tel);
        System.out.println(User.tel);
        pst3.setString(2,id);
        pst3.executeUpdate();
        user.setSeat(1);
        user.setRoomId(id);

        String sql4 = "update selfRoom set master=? where selfRoomId=?";//在master中加入用户id
        PreparedStatement pst4 = con.prepareStatement(sql4);
        pst4.setString(1,User.tel);
        pst4.setString(2,id);
        pst4.executeUpdate();
        user.isMaster = true;//设为房主


        pst1.close();
        pst2.close();
        pst3.close();
        pst4.close();
    }
    //创建贝壳房间
    public void createShellRoom(String name,String password) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//包含第一个人进房间的信息
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();

        String id;
        ResultSet resultSet = null;
        do {
            id = utils.shellId();//不一样！！！
            String sql = "select * from selfRoom where selfRoomId=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            resultSet = pst.executeQuery();
        } while (resultSet.next());//当数据库已经存在这个id，就继续随机生成新的id

        //对selfRoom表：id随机生成 name：输入获取（2-10） notice:进去修改（null） password:有和无 memberNumber:1
        String sql1 = "insert into selfRoom (selfRoomId,name,password,memberNumber) values (?,?,?,?)";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1, id);
        pst1.setString(2, name);
        pst1.setString(3, password);
        pst1.setInt(4, 1);//成员人数默认为1
        pst1.executeUpdate();

        String sql2 = "update users set selfRoomId=? where phoneNumber=?";//在用户中添加房间id
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setString(1, id);
        pst2.setString(2, User.tel);//获取手机号
        System.out.println("创建房间"+User.tel);
        pst2.executeUpdate();

        String sql3 = "update selfRoom set seat1=? where selfRoomId=?";//在seat1中加入用户id
        PreparedStatement pst3 = con.prepareStatement(sql3);
        pst3.setString(1, User.tel);
        pst3.setString(2,id);
        pst3.executeUpdate();
        User user = new User();
        user.setSeat(1);
        user.setRoomId(id);

        String sql4 = "update selfRoom set master=? where selfRoomId=?";//在master中加入用户id
        PreparedStatement pst4 = con.prepareStatement(sql4);
        pst4.setString(1, User.tel);
        pst4.setString(2,id);
        pst4.executeUpdate();
        user.isMaster = true;//设为房主

        pst1.close();
        pst2.close();
        pst3.close();
        pst4.close();
    }
    //验证手机号和密码是否正确
    public boolean existPhoneAndPass(String phone,String password) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select * from users where phoneNumber=? and password=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, phone);
        pst.setString(2, password);

        if (pst.executeQuery().next()) //如果找到手机号码和密码
            return true;
        else
            return false;
    }
    //验证手机号是否存在
    public boolean existPhone(String phone) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select * from users where phoneNumber=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,phone);
        if(pst.executeQuery().next())//如果找到密码
            return true;
        else
            return false;
    }

    public void signUp(String name,String pwd,String phone) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "insert into users(username,password,phoneNumber) values(?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,name);
        pst.setString(2,pwd);
        pst.setString(3,phone);
        pst.executeUpdate();
        pst.close();
    }

    public void changePass(String pwd) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "UPDATE users SET password=? WHERE phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, pwd);
        pst.setString(2,User.tel);
        pst.executeUpdate();//更新数据库
        pst.close();

    }

    //是否找到该房间
    public boolean findRoom(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select * from selfRoom where selfRoomId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        if (pst.executeQuery().next()) {
            pst.close();
            return true;
        }else{
            pst.close();
            return false;
        }
    }
    //是否有设置密码（房间）
    public boolean hasPwd(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select password from selfRoom where selfRoomId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            String pwd = rs.getString(1);
            if(pwd!=null){//如果字符串非空：有密码
                pst.close();
                return true;
            }else
                return false;
        }
        pst.close();
        return false;
    }
    //在有密码的情况下查找密码（房间）
    public boolean pwdFound(String id,String pwd) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select * from selfRoom where selfRoomId=? and password=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        pst.setString(2,pwd);
        if (pst.executeQuery().next()) {
            pst.close();
            return true;
        }else{
            pst.close();
            return false;
        }
    }
    //找房间的名字
    public String findRoomName(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select name from selfRoom where selfRoomId=?";//通过id查找名字
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            String roomName = rs.getString(1);
            pst.close();
            return roomName;
        }
        pst.close();
        return null;
    }
    //修改房间名字
    public void updateRoomName(String name) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "UPDATE selfRoom SET name=? WHERE selfRoomId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,name);
        pst.setString(2,user.getRoomId());
        pst.executeUpdate();//更新数据库
        pst.close();
    }
    //通过id找现在有的所有名字
    public boolean existName(String name) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select name from selfRoom where selfRoomId=?";//通过id查找名字
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,name);
        ResultSet rs = pst1.executeQuery();
        if(rs.next()){
            String roomName = rs.getString(1);
            pst1.close();
            return true;
        }
        pst1.close();
        return false;
    }
    //查找某个id的公告
    public String findRoomAnnouncement(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select notice from selfRoom where selfRoomId=?";//通过id查找名字
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            String announcement = rs.getString(1);
            pst.close();
            return announcement;
        }
        pst.close();

        return null;
    }
    //修改房间公告
    public void updateRoomAnnouncement(String notice) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "UPDATE selfRoom SET notice=? WHERE selfRoomId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,notice);
        pst.setString(2,user.getRoomId());
        pst.executeUpdate();//更新数据库
        pst.close();
    }
    //找成员的个数
    public int findMemberNumber(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select memberNumber from selfRoom where selfRoomId=?";//通过id查找名字
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            int number = rs.getInt(1);
            pst.close();
            return number;
        }
        pst.close();
        return 0;
    }
    //该房间是否满人:在已经找到房间的前提下
    public boolean isFull(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select memberNumber from selfRoom where selfRoomId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet resultSet = pst.executeQuery();
        //处理结果集:如果找到了 就返回结果中的数字
        if(resultSet.next()) {//next():判断结果集的下一条是否有数据，如果有数据返回true,并指针下移；如果返回false,指针不会下移。
            int result = resultSet.getInt(1);
            if(result >= 4){//如果大于四 满人
                pst.close();
                return true;
            }else{
                pst.close();
                return false;
            }
        }else{
            pst.close();
            return true;
        }
    }
    //进入房间
    public void enterRoom(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//进入房间（在房间已经建立的情况下）
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        //补：房间：人数+1  用户：加入房间id
        String sql1 = "update selfRoom set memberNumber=memberNumber+1 where selfRoomId=?";//房间人数+1
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,id);
        pst1.executeUpdate();

        String sql2 = "update users set selfRoomId=? where phoneNumber=?";//在用户中添加房间id
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setString(1,id);
        pst2.setString(2,User.tel);//获取手机号
        pst2.executeUpdate();

        //查询4个座位
        String sql3 = "select seat1,seat2,seat3,seat4 from selfRoom where selfRoomId=?";
        PreparedStatement pst3 = con.prepareStatement(sql3);
        pst3.setString(1,id);
        ResultSet rs = pst3.executeQuery();
        String seat[] = new String[4];
        if(rs.next()){
            seat[0] = rs.getString(1);
            seat[1] = rs.getString(2);
            seat[2] = rs.getString(3);
            seat[3] = rs.getString(4);
        }

        //寻找不为0的
        int i = 0;
        while(seat[i]!=null&&i<4)
            i++;

        //新数组传回数据库
        String sql4 = "update selfRoom set seat" + (i+1) + "=? where selfRoomId=?";
        PreparedStatement pst4 = con.prepareStatement(sql4);
        pst4.setString(1,User.tel);
        pst4.setString(2,id);
        pst4.executeUpdate();

        //记住房间号
        User user = new User();
        user.setRoomId(id);
        user.setSeat(i+1);

        pst1.close();
        pst2.close();
        pst3.close();
        pst4.close();
    }
    //退出房间
    public void exitRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//退出房间
        Connection con = null;//连接数据库
        User user = new User();

        con = JdbcUtils.getConn();
        //补：房间：人数-1  用户：删除房间id
        String sql1 = "update selfRoom set memberNumber=memberNumber-1 where selfRoomId=(select selfRoomId from users where phoneNumber=?)";//房间人数-1
        PreparedStatement pst1 = con.prepareStatement(sql1);
        System.out.println("退出房间"+User.tel);
        pst1.setString(1,User.tel);
        pst1.executeUpdate();

        String sql2 = "update users set selfRoomId=-1 where phoneNumber=?";//-1代表不进入房间
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setString(1,User.tel);//获取手机号
        pst2.executeUpdate();

        String sql3 = "update selfRoom set seat"+user.getSeat()+"=null where selfRoomId=?";//删除座位信息
        PreparedStatement pst3 = con.prepareStatement(sql3);
        pst3.setString(1, user.getRoomId());
        pst3.executeUpdate();

        user.isMaster = false;//房主取消

        //换房主
        //查询4个座位
        String sql4 = "select seat1,seat2,seat3,seat4,memberNumber from selfRoom where selfRoomId=?";
        PreparedStatement pst4 = con.prepareStatement(sql4);
        pst4.setString(1,user.getRoomId());
        ResultSet rs = pst4.executeQuery();
        String seat[] = new String[4];
        if(rs.next()){
            seat[0] = rs.getString(1);
            seat[1] = rs.getString(2);
            seat[2] = rs.getString(3);
            seat[3] = rs.getString(4);
        }

        int memberNumber = rs.getInt(5);
        int i = 0;
        //寻找不为0的
        if(memberNumber!=0) {
            while (seat[i] == null)
                i++;
        }

        String sql5 = "update selfRoom set master="+seat[i]+" where selfRoomId=?";//删除座位信息
        PreparedStatement pst5= con.prepareStatement(sql5);
        pst5.setString(1, user.getRoomId());
        pst5.executeUpdate();

        user.setRoomId(-1+"");

        pst1.close();
        pst2.close();
        pst3.close();
        pst4.close();
        pst5.close();
    }
    //获取房主id
    public String getMasterId(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select master from selfRoom where selfRoomId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet resultSet = pst.executeQuery();
        //处理结果集:如果找到了 就返回结果中的数字
        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return "";
    }
    //删除人数为0的房间
    public void deleteRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//销毁一下房间
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "delete from selfRoom where memberNumber=0";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.executeUpdate();
        pst.close();

    }
    //存在房间的id的所有信息
    public ArrayList<String> existUndergroundRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//返回现在正在直播的房间id
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select selfRoomId from selfRoom where memberNumber!=0";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> templist = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();

        while(rs.next()) {
                String s = rs.getString(1);
                templist.add(s);
        }
        for(int i = 0;i < templist.size();i++){
            if(templist.get(i).charAt(0)=='1')
                list.add(templist.get(i));
        }
        rs.close();
        pst.close();
        return list;
    }

    public ArrayList<String> existTreeRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//返回现在正在直播的房间id
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select selfRoomId from selfRoom where memberNumber!=0";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> templist = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();

        while(rs.next()) {
            String s = rs.getString(1);
            templist.add(s);
        }
        for(int i = 0;i < templist.size();i++){
            if(templist.get(i).charAt(0)=='2')
                list.add(templist.get(i));
        }
        rs.close();
        pst.close();
        return list;
    }

    public ArrayList<String> existShellRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {//返回现在正在直播的房间id
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select selfRoomId from selfRoom where memberNumber!=0";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> templist = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();

        while(rs.next()) {
            String s = rs.getString(1);
            templist.add(s);
        }
        for(int i = 0;i < templist.size();i++){
            if(templist.get(i).charAt(0)=='3')
                list.add(templist.get(i));
        }
        rs.close();
        pst.close();
        return list;
    }
    //存在放房间的数量
    public int existUndergroundRoomNumber() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select selfRoomId from selfRoom where memberNumber!=0";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        int number = 0;
        ArrayList<String> list = new ArrayList<>();

        while(rs.next()) {
            String s = rs.getString(1);
            list.add(s);
        }
        for(int i = 0;i <list.size();i++){
            if(list.get(i).charAt(0)=='1')
                number++;
        }
        pst.close();
        return number;
    }

    public int existTreeRoomNumber() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select selfRoomId from selfRoom where memberNumber!=0";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        int number = 0;
        ArrayList<String> list = new ArrayList<>();

        while(rs.next()) {
            String s = rs.getString(1);
            list.add(s);
        }
        for(int i = 0;i <list.size();i++){
            if(list.get(i).charAt(0)=='2')
                number++;
        }
        pst.close();
        return number;
    }

    public int existShellRoomNumber() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select selfRoomId from selfRoom where memberNumber!=0";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        int number = 0;
        ArrayList<String> list = new ArrayList<>();

        while(rs.next()) {
            String s = rs.getString(1);
            list.add(s);
        }
        for(int i = 0;i <list.size();i++){
            if(list.get(i).charAt(0)=='3')
                number++;
        }
        pst.close();
        return number;
    }
    //根据手机号查找房间id
    public String findRoomId() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select selfRoomId from users where phoneNumber=?";//通过id查找名字
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
           String roomId = rs.getString(1);
            pst.close();
            return roomId;
        }
        pst.close();
        return null;
    }

    public String[] findSeat() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select seat1,seat2,seat3,seat4 from selfRoom where selfRoomId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,user.getRoomId());
        ResultSet rs = pst.executeQuery();
        String seat[] = new String[4];
        if(rs.next()){
            seat[0] = rs.getString(1);
            seat[1] = rs.getString(2);
            seat[2] = rs.getString(3);
            seat[3] = rs.getString(4);
        }
        pst.close();
        return seat;
    }

    public void setOrnament(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set ornament=? where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public void setNeck(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set neckdress=? where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public void setHead(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set headdress=? where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public void setAnimal(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set animal=? where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public String getOrnament() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select ornament from users where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            String id = rs.getString(1);
            pst.close();
            return id;
        }
        pst.close();
        return null;
    }

    public String getNeck() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select neckdress from users where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            String id = rs.getString(1);
            pst.close();
            return id;
        }
        pst.close();
        return null;
    }

    public String getHead() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select headdress from users where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            String id = rs.getString(1);
            pst.close();
            return id;
        }
        pst.close();
        return null;
    }


    public String getAnimal() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select animal from users where phoneNumber=?";//找到用户的id
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            String id = rs.getString(1);
            pst.close();
            return id;
        }
        pst.close();
        return null;
    }

    public boolean isBought(int situation) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql;
        switch (situation){
            case 11://头饰
                sql = "select h1 from users where phoneNumber=?";
                break;
            case 12:
                sql = "select h2 from users where phoneNumber=?";
                break;
            case 13:
                sql = "select h3 from users where phoneNumber=?";
                break;
            case 14:
                sql = "select h4 from users where phoneNumber=?";
                break;
            case 21://颈饰
                sql = "select n1 from users where phoneNumber=?";
                break;
            case 22:
                sql = "select n2 from users where phoneNumber=?";
                break;
            case 23:
                sql = "select n3 from users where phoneNumber=?";
                break;
            case 24:
                sql = "select n4 from users where phoneNumber=?";
            case 31://摆件
                sql = "select o1 from users where phoneNumber=?";
                break;
            case 32:
                sql = "select o2 from users where phoneNumber=?";
                break;
            case 33:
                sql = "select o3 from users where phoneNumber=?";
                break;
            case 34:
                sql = "select o4 from users where phoneNumber=?";
                break;
            default:
                sql = null;
                break;
        }
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        int result = rs.getInt(1);
        if(result==0){
            pst.close();
            return false;
        }else{
            pst.close();
            return true;
        }
    }

    public void buy(int situation) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql;
        switch (situation){
            case 11:
                sql = "update users set h1=1 where phoneNumber=?";
                break;
            case 12:
                sql = "update users set h2=1 where phoneNumber=?";
                break;
            case 13:
                sql = "update users set h3=1 where phoneNumber=?";
                break;
            case 14:
                sql = "update users set h4=1 where phoneNumber=?";
                break;
            case 21:
                sql = "update users set n1=1 where phoneNumber=?";
                break;
            case 22:
                sql = "update users set n2=1 where phoneNumber=?";
                break;
            case 23:
                sql = "update users set n3=1 where phoneNumber=?";
                break;
            case 24:
                sql = "update users set n4=1 where phoneNumber=?";
                break;
            case 31:
                sql = "update users set o1=1 where phoneNumber=?";
                break;
            case 32:
                sql = "update users set o2=1 where phoneNumber=?";
                break;
            case 33:
                sql = "update users set o3=1 where phoneNumber=?";
                break;
            case 34:
                sql = "update users set o4=1 where phoneNumber=?";
                break;
            default:
                sql=null;
                break;
        }
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public String findInterpre(int kind,String name) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql;

        switch (kind) {
            case 1://英-》中
                sql = "select interpretation from English where name=?";
                break;
            case 2://日-》中
                sql = "select interpretation from Japanese where name=?";
                break;
            default:
                sql = null;
        }

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,name);
        ResultSet rs = pst.executeQuery();
        String intepretation = null;
        if(rs.next())
            intepretation = rs.getString(1);
        pst.close();
        return intepretation;
    }

    public int getCoin() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select coin from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        int result;
        if(rs.next()) {
            result = rs.getInt(1);
        }else{
            result = 0;
        }
        return result;
    }

    public void setCoin(int coin) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set coin=? where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1,coin);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public String getName() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select username from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        String name = null;
        if(rs.next()) {
            name = rs.getString(1);
        }
        return name;
    }

    public int getCourseBuy() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select hasBoughtClass from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        int result=0;
        if(rs.next()) {
            result = rs.getInt(1);
        }
        return result;
    }
    public int getCourseTeach() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select hasToughtClass from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        int result=0;
        if(rs.next()) {
            result = rs.getInt(1);
        }
        return result;
    }

    public String getIntro() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select teacherIntro from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        String name = null;
        if(rs.next()) {
            name = rs.getString(1);
        }
        return name;
    }

    public void setName(String name) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set username=? where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,name);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public void setIntro(String intro) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set teacherIntro=? where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,intro);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }
    public void setCover(String qual) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update livingRoom set cover=? where teacherId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,qual);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }
    public String getCover() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select cover from livingRoom where teacherId=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        String qual = null;
        if(rs.next()) {
            qual = rs.getString(1);
        }
        pst.close();
        return qual;
    }
    public void setQual(String qual) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set teacherQuali=? where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,qual);
        pst.setString(2,User.tel);
        pst.executeUpdate();
        pst.close();
    }
    public String getQual() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select teacherQuali from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        String qual = null;
        if(rs.next()) {
            qual = rs.getString(1);
        }
        pst.close();
        return qual;
    }

    public void creatLivingRoom(String name,String password,String language,String introduction) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        //id
        String id;
        ResultSet resultSet = null;
        do {
            id = utils.livingId();//不一样！！！
            String sql1 = "select * from livingRoom where id=?";
            PreparedStatement pst1 = con.prepareStatement(sql1);
            pst1.setString(1, id);
            resultSet = pst1.executeQuery();
        } while (resultSet.next());//当数据库已经存在这个id，就继续随机生成新的id
        String sql2 = "insert into livingRoom (id,name,password,teacherId,language,introduction) values (?,?,?,?,?,?)";
        PreparedStatement pst2=con.prepareStatement(sql2);
        pst2.setString(1,id);//id
        pst2.setString(2,name);//房间名
        pst2.setString(3,password);//密码
        pst2.setString(4,User.tel);//房主id
        pst2.setString(5,language);//语言
        pst2.setString(6,introduction);//简介

        user.roomId=id;
        System.out.println("数据库里user.roomId:"+user.getRoomId());
        user.roomName=name;
        for(int i=0; i < 2;i++){
            System.out.println("database"+ roomId + user.roomName);
        }
        pst2.executeUpdate();

        pst2.close();

    }

    public void enterLivingRoom(String roomId) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "update users set livingRoomId=? where phoneNumber=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,roomId);
        pst1.setString(2,User.tel);
        pst1.executeUpdate();

        String sql2 = "update livingRoom set number=number+1 where id=?";
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setString(1,roomId);
        pst2.executeUpdate();

        User.roomId =roomId;

        String sql3 = "select name from livingRoom where id=?";
        PreparedStatement pst3 = con.prepareStatement(sql3);
        pst3.setString(1,roomId);
        ResultSet rs = pst3.executeQuery();
        if(rs.next())
            user.setRoomName(rs.getString(1));

        pst1.close();
        pst2.close();
        pst3.close();
    }

    public boolean hasQual() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select quali from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, User.tel);
        ResultSet rs = pst.executeQuery();
        int qual=0;
        if(rs.next()){
            qual = rs.getInt(1);//如果有教资
            if(qual==1)
                return true;
        }
        return false;
    }



    public int getLivingRoomNumber() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select * from livingRoom";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        ResultSet rs = pst1.executeQuery();
        int count = 0;
        while(rs.next())
            count++;
        return count;
    }

    public ArrayList<classroom> getWholeClassroom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ArrayList<classroom> temp=new ArrayList<classroom>();
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select * from livingRoom";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        ResultSet rs1 = pst1.executeQuery();
        while(rs1.next()){
            String teacherId = rs1.getString(1);
            System.out.println("teacherId = "+teacherId);
            String sql2 = "select username,teacherIntro,quali from users where phoneNumber=?";
            PreparedStatement pst2 = con.prepareStatement(sql2);
            pst2.setString(1,teacherId);
            ResultSet rs2=pst2.executeQuery();
            instructor instructor;
            if(rs2.next()) {
                if (rs2.getInt(3) == 0) {//无教资
                    instructor = new instructor(rs2.getString(1), rs2.getString(2), false);//名称 简介 是否认证
                } else {
                    instructor = new instructor(rs2.getString(1), rs2.getString(2), true);//名称 简介 是否认证
                }
            }else
                instructor = new instructor("陈老师","暂无",false);

            classroom classroom = new classroom();
            classroom.id = rs1.getString(3);
            classroom.roomName =rs1.getString(4);
            classroom.password=rs1.getString(5);
            classroom.instructor = instructor;
            classroom.roomIntroduction = rs1.getString(7) ;
            classroom.cover=rs1.getString(2);
            String language = rs1.getString(6);
            switch (language){
                case "英 语":
                    classroom.tag= ENGLISH;//语言
                    break;
                case "日 语":
                    classroom.tag= JAPANESE;//语言
                    break;
                case "法 语":
                    classroom.tag= FRENCH;//语言
                    break;
                default:
                    classroom.tag= OTHERS;//语言
                    break;
            }
            temp.add(classroom);
        }
        return temp;
    }

    public int getLivingroomMemberNumber() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select number from livingRoom where id=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,user.getRoomId());
        ResultSet rs = pst1.executeQuery();
        int number = 0;
        if(rs.next())
            number = rs.getInt(1);
        return number;
    }

    public String findName(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select username from users where phoneNumber=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,id);
        ResultSet rs = pst1.executeQuery();
        String name = null;
        if(rs.next())
           name = rs.getString(1);
        return name;
    }

    public void addQuestion() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "update livingRoom set question=question+1 where id=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,user.getRoomId());
        pst1.executeUpdate();
        pst1.close();
    }

    public int getQuestion() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select question from livingRoom where id=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,user.getRoomId());
        ResultSet rs = pst1.executeQuery();
        int number = 0;
        if(rs.next())
            number = rs.getInt(1);
        return number;
    }

    public void updateQuestion() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "update livingRoom set question=0 where id=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,user.getRoomId());
        pst1.executeUpdate();
        pst1.close();
    }

    public classroom getClassroom(String id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();


        String sql = "select * from livingRoom where id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet rs=pst.executeQuery();
        instructor instructor;
        if(rs.next()) {//找到房间
            String sql1 = "select username,teacherIntro,quali from users where phoneNumber=?";
            PreparedStatement pst1 = con.prepareStatement(sql1);
            pst1.setString(1,User.tel);
            ResultSet rs1=pst1.executeQuery();
            if(rs1.next()) {
                if (rs1.getInt(3) == 0) {//无教资
                    instructor = new instructor(rs1.getString(1), rs1.getString(2), false);//名称 简介 是否认证
                } else {
                    instructor = new instructor(rs1.getString(1), rs1.getString(2), true);//名称 简介 是否认证
                }
            }else {
                instructor = new instructor(rs1.getString(1), rs1.getString(2), true);//名称 简介 是否认证
            }
            pst1.close();
        }else
            instructor = new instructor("陈老师","暂无",false);

        classroom classroom = new classroom();
        classroom.id = rs.getString(3);
        classroom.roomName =rs.getString(4);
        classroom.password=rs.getString(5);
        classroom.instructor = instructor;
        classroom.roomIntroduction = rs.getString(7) ;
        classroom.cover=rs.getString(2);
        String language = rs.getString(6);
        switch (language){
            case "英 语":
                classroom.tag= ENGLISH;//语言
                break;
            case "日 语":
                classroom.tag= JAPANESE;//语言
                break;
            case "法 语":
                classroom.tag= FRENCH;//语言
                break;
            default:
                classroom.tag= OTHERS;//语言
                break;
        }

        pst.close();
        return classroom;
    }

    public void leaveLivingroom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "update users set livingRoomId=null where phoneNumber=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,user.getRoomId());
        pst1.executeUpdate();
        pst1.close();

        roomId=null;
    }

    public void deleteLivingroom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "update users set livingRoomId=null where phoneNumber=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,User.tel);
        pst1.executeUpdate();

        String sql2 = "delete from livingRoom where id=?";
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setString(1,user.getRoomId());
        pst2.executeUpdate();

        pst1.close();
        roomId=null;
    }

    public ArrayList<News> getTopic() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql;
        PreparedStatement pst;
        ResultSet rs;
        ArrayList<News> temp = new ArrayList<>();
        News topic= new News();
        //生成随机数组
        int[] ids = utils.randomCommon(1,20,10);
        //以数组为id查找问题
        for(int i = 0;i<10;i++){
            sql = "select title,content from topic where id=?";
            pst = con.prepareStatement(sql);
          //  System.out.println(ids[i]);
            pst.setInt(1,ids[i]);
            rs = pst.executeQuery();
            if(rs.next()){
                topic.title = rs.getString(1);
                topic.content = rs.getString(2);
                temp.add(topic);
            }
        }
        return temp;
    }

    public void setOral(int Eng,int Fra,int Jan,int Other) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "update users set English=?,Fran=?,Janpa=?,Other=? where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1,Eng);
        pst.setInt(2,Fra);
        pst.setInt(3,Jan);
        pst.setInt(4,Other);
        pst.setString(5,User.tel);
        pst.executeUpdate();
        pst.close();
    }

    public int[] getOral() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select English,Fran,Janpa,Other from users where phoneNumber=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,User.tel);
        ResultSet rs = pst1.executeQuery();
        int[] temp = new int[4];
        if(rs.next()){
            temp[0]=rs.getInt(1);
            temp[1]=rs.getInt(2);
            temp[2]=rs.getInt(3);
            temp[3]=rs.getInt(4);
        }
        return temp;
    }

    public void creatOralRoom(int type,String pass,int fee) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 =  "insert into oralRoom (id,teacherId,type,password,fee) values (?,?,?,?,?)";
        PreparedStatement pst1 = con.prepareStatement(sql1);

        String roomId = utils.random2();
        String sql2 =  "select id from oralRoom";
        PreparedStatement pst2 = con.prepareStatement(sql2);
        ResultSet rs2 = pst2.executeQuery();
        while(rs2.next()){
            if(roomId.equals(rs2.getString(1)))
                roomId = utils.random2();
        }

        pst1.setString(1,roomId);
        pst1.setString(2,User.tel);
        pst1.setInt(3,type);
        pst1.setString(4,pass);
        pst1.setInt(5,fee);

        pst1.executeUpdate();

        String sql3 =  "update users set oralRoom=? where phoneNumber=?";
        PreparedStatement pst3 = con.prepareStatement(sql3);
        pst3.setString(1,roomId);

        pst3.setString(2, User.tel);
        pst3.executeUpdate();

        User.roomId = roomId;
        System.out.println("创建房间，roomid="+roomId);

        pst1.close();
        pst2.close();
        pst3.close();

    }


    public boolean enterOralRoom(String roomId) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();

        System.out.println("roomid="+roomId);
        String sql = "select studentId from oralRoom where id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,roomId);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            if(rs.getString(1)==null) {//无学生
                String sql1 = "update users set OralRoom=? where phoneNumber=?";
                PreparedStatement pst1 = con.prepareStatement(sql1);
                pst1.setString(1,roomId);
                pst1.setString(2,User.tel);
                pst1.executeUpdate();

                String sql2 = "update oralRoom set studentId=? where id=?";
                PreparedStatement pst2 = con.prepareStatement(sql2);
                pst2.setString(1, User.tel);
                pst2.setString(2,roomId);
                pst2.executeUpdate();
                System.out.println("studentid="+User.tel+"  "+"roomId"+User.roomId);

                User.roomId = roomId;
                pst1.close();
                pst2.close();
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public void leaveOralRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();

        System.out.println("离开房间");
        //判断老师还是学生
        String roomId = User.roomId;
        String sql = "select studentId,teacherId from oralRoom where id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,roomId);
        ResultSet rs = pst.executeQuery();
        String stu ;
        String tea;
        if(rs.next()){
            stu = rs.getString(1);
            tea = rs.getString(2);
            System.out.println("学生id："+stu+" 老师id："+tea);
            if(User.tel.equals(stu)) {//是学生
                System.out.println("id是学生");
                String sql2 = "update oralRoom set studentId=? where id=?";
                PreparedStatement pst2 = con.prepareStatement(sql2);
                pst2.setString(1, null);
                pst2.setString(2,User.roomId);
                System.out.println("roomId="+User.roomId);
                pst2.executeUpdate();
                pst2.close();
            }

            if(User.tel.equals(tea)){//是老师
                System.out.println("id是老师");
                String sql2 = "delete from oralRoom set where id=?";
                PreparedStatement pst2 = con.prepareStatement(sql2);
                pst2.setString(1, User.roomId);
                System.out.println("离开"+"roomId="+User.roomId);
                pst2.executeUpdate();
                pst2.close();
            }
        }



        pst.close();
    }

    public ArrayList<oralroom> getOralRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        ArrayList<oralroom> temp = new ArrayList<oralroom>();
        String teacherId = new String();
        String sql1 = "select teacherId,studentId,type,password,fee,id from oralRoom";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        ResultSet rs1 = pst1.executeQuery();
        while (rs1.next()){
            oralroom room = new oralroom();
            oralUser user = new oralUser(R.drawable.logo,null,null,null, oralUser.HANGOUT);
            int eng = 0;
            int fra = 0;
            int jan = 0;
            int other = 0;
            teacherId = rs1.getString(1);
            String sql2="select username,English,Fran,Janpa,Other,oralIntro from users where phoneNumber=?";
            PreparedStatement pst2 = con.prepareStatement(sql2);
            pst2.setString(1,teacherId);
            ResultSet rs2 = pst2.executeQuery();
            if(rs2.next()) {
                eng = rs2.getInt(2);
                fra = rs2.getInt(3);
                jan = rs2.getInt(4);
                other = rs2.getInt(5);
                room.roomIntroduction=rs2.getString(6);

                List<List<Language>> threeLists=setLanguages(eng,fra,jan,other);
                Language[] mother= toArray(threeLists.get(0));
                Language[] good=toArray(threeLists.get(1));
                Language[] learn=toArray(threeLists.get(2));
                user = new oralUser(R.drawable.logo,mother,good,learn, oralUser.HANGOUT);
            }

            room.id=rs1.getString(6);
            room.teacherName=rs2.getString(1);
            room.user = user;
            room.type = rs1.getInt(3);
            room.password = rs1.getString(4);

            //member
            if(rs1.getString(2)==null)
                room.roomMember=1;
            else
                room.roomMember=2;

            //iscourse
            if(room.type==2)
                room.isCourse = true;
            else
                room.isCourse = false;

            temp.add(room);
        }
        return temp;
    }

    //getOralRoom()用
    public Language[] toArray(List<Language> list){
        Language[] array=new Language[4];
        for(int i=0;i<list.size();i++)
            array[i]=list.get(i);
        return array;
    }
    //getOralRoom()用
    public List<List<Language>> setLanguages(int E,int F,int J,int O){
        List<List<Language> > list=new ArrayList<>();
        List<Language> mother=new ArrayList<>(),good=new ArrayList<>(),learn=new ArrayList<>();
        list.add(mother);
        list.add(good);
        list.add(learn);

        if(E!=0)
            list.get(E-1).add(ENGLISH);
        if(F!=0)
            list.get(F-1).add(FRENCH);
        if(J!=0)
            list.get(J-1).add(JAPANESE);

        if(O!=0){
            if(O==1||O==4||O==5||O==7)
                list.get(0).add(OTHERS);

            if(O==2||O==4||O==6||O==7)
                list.get(1).add(OTHERS);

            if(O==3||O==5||O==6||O==7)
                list.get(2).add(OTHERS);
        }
        return list;
    }

    public String getOralIntro() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "select oralIntro from users where phoneNumber=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,User.tel);
        ResultSet rs = pst1.executeQuery();
        String s = new String();
        if(rs.next()){
            s = rs.getString(1);
        }
        pst1.close();
        return s;
    }


    public void setOralIntro(String s) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql1 = "update users set oralIntro=? where phoneNumber=?";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setString(1,s);
        pst1.setString(2,user.getRoomId());
        pst1.executeUpdate();
        pst1.close();
    }

    public boolean findOralRoom(String roomId) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(roomId==null)
            return false;

        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select studentId from oralRoom where id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,roomId);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            if(rs.getString(1)!=null)//有学生
                return false;
            else
                return true;
        }
        return false;
    }

    public String getOralPass(String roomId) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select password from oralRoom where id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,roomId);
        ResultSet rs = pst.executeQuery();
        String pass = null;
        if(rs.next()) {
            pass = rs.getString(1);
        }
        return pass;
    }

    public int getOralFee(String roomId) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select fee from oralRoom where id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,roomId);
        ResultSet rs = pst.executeQuery();
        int fee = 0;
        if(rs.next()) {
            fee = rs.getInt(1);
        }
        return fee;
    }

    public boolean payFee(int fee) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select coin from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, User.tel);
        ResultSet rs = pst.executeQuery();
        int coin = 0;
        if(rs.next()) {
            if(!(rs.getInt(1)>=fee))//不够钱
                return false;
            else{//够钱
                String sql2 = "update users set coin=coin-"+fee+" where phoneNumber=?";
                PreparedStatement pst2 = con.prepareStatement(sql2);
                pst2.setString(1, User.tel);
                pst2.executeUpdate();
                return true;
            }
        }
        return false;
    }

    public String getUri() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection con = null;//连接数据库
        con = JdbcUtils.getConn();
        String sql = "select teacherQuali from users where phoneNumber=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,User.tel);
        ResultSet rs = pst.executeQuery();
        String uri=null;
        if(rs.next()) {
            uri = rs.getString(1);
        }
        pst.close();
        return uri;
    }

   public boolean isFirst() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //如果数据库中的isFirst是0，就是第一次，是1，就不是第一次
       Connection con = null;//连接数据库
       con = JdbcUtils.getConn();
       String sql = "select isFirst from users where phoneNumber=?";
       PreparedStatement pst = con.prepareStatement(sql);
       pst.setString(1,User.tel);
       ResultSet rs = pst.executeQuery();
       int isFirst =0;
       if(rs.next()) {
           isFirst = rs.getInt(1);
           if(isFirst == 1)//不是第一次，返回false
               return false;
           else //是第一次，先改值，再返回
           {
               String sql2 = "update users set isFirst="+1+" where phoneNumber=?";
               PreparedStatement pst2 = con.prepareStatement(sql2);
               pst2.setString(1, User.tel);
               pst2.executeUpdate();
               pst2.close();
               return true;
           }
       }
       pst.close();
       return true;
   }

   public void exitOralRoom() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

       Connection con = null;//连接数据库
       con = JdbcUtils.getConn();
       //补：房间：人数-1  用户：删除房间id
       String sql1 = "update selfRoom set memberNumber=memberNumber-1 where selfRoomId=(select selfRoomId from users where phoneNumber=?)";//房间人数-1
       PreparedStatement pst1 = con.prepareStatement(sql1);
       System.out.println("退出房间"+User.tel);
       pst1.setString(1,User.tel);
       pst1.executeUpdate();

   }

}
