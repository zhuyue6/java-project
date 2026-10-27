## 申请https 证书

使用 acme.sh 

# 克隆国内gitee镜像
git clone https://gitee.com/neilpang/acme.sh.git
cd acme.sh
# 安装，邮箱随便填一个
./acme.sh --install -m test@example.com

使用aliy ram 用户申请 ak 

# 当前环境执行设置阿里云密钥
```sh
export Ali_Key="你的AccessKeyID"
export Ali_Secret="你的AccessKeySecret"
```

# 申请证书（泛域名示例）
```sh

acme.sh --issue --dns dns_ali -d zywyy.cn -d *.zywyy.cn
# 如果被墙了 使用这个
acme.sh --set-default-ca --server zerossl
```


# 安装证书到nginx（和挂载目录对齐）
你 nginx 挂载：`./nginx/nginx.conf`
新建证书目录：`mkdir -p ./nginx/ssl`

acme.sh --install-cert -d zywyy.cn \
--key-file       /opt/apps/clock-my-goal/docker/nginx/ssl/zywyy.key  \
--fullchain-file /opt/apps/clock-my-goal/docker/nginx/ssl/zywyy.crt \
--reloadcmd     "docker exec nginx nginx -s reload"