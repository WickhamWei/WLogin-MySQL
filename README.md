## WLogin  

目前只支持纯MySQL模式  
### 玩家功能  

- [x] 在玩家未登录时玩家的位置和行为将被锁定。  
- [x] 使用指令进行注册和登录，修改密码。  
*  `/login <password>`登录  
*  `/register <password> <password>`注册  
*  `/changepassword <oldpassword> <newpassword> <newpassword>`修改密码   
- [x] 查询自己以前所有登录的时间和IP地址，包括登录成功或者失败的行为。  
*  `/logindata [page]`查询登录记录  
*  `/loginfaildata [page]`查询登录失败记录
- [x] 每次游戏游玩的时间，总的游戏时间等。  
*  `/playingtime`查询游戏时间记录
- [x] 在不同IP进行登录将收到提醒。  
- [x] 防沉迷系统。（管理员命令）  
*  `/teenagers <playername>`将玩家设置成防沉迷对象
*  `/deteenagers <playername>`将玩家取消防沉迷对象
*  `/isteenagers <playername>`查询玩家是否是防沉迷对象
*  **防沉迷对象游戏时间每天不可大于三个小时**


### 管理员功能  

- [x] 游戏管理员拥有以上普通玩家的所有权限。  
- [x] 查询所有人的所有登录时的信息，包括成功或者失败的信息，登录时的IP地址，时间。  
*  `/logindata <playername> [page]`查询登录记录  
*  `/loginfaildata <playername> [page]`查询登录失败记录
- [x] 查询玩家总的游戏时长。
*  `/playingtime <playername>`查询游戏时间记录
- [x] 玩家登录失败警告。  
- [x] 修改密码的时间，IP地址等。  
*  `/passwordchangehistory <playername>`查询密码修改记录
- [x] 对玩家进行封禁，可选封禁时长，封禁理由等等。  
*  `/wban <playername> <banTimeMin> <reason>`封禁玩家
*  `/wdean <playername>`取消玩家现有的封禁
*  `/wbandata <playername> [page]`查询玩家的封禁记录
*  `/wisban <playername>`玩家是否被封禁 
*  **封禁时间不是叠加，而是分开且同时计算** 
*  **取消封禁将删除涉及正在封禁的所有记录，只保留已服刑完毕的记录** 

### 下载地址  

[发布页面](https://github.com/WickhamWei/WLogin/releases)