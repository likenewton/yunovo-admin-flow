###!/usr/bin
###描述：jenkins 压缩文件夹专用
###作者：张川
###############
project_path=/root/.jenkins/workspace/$1
weixin_name=flowCenter
git_branch=$2
git_branch_commitID=$3



if [  -d "${project_path}" ];then
	
	cd ${project_path}
	rm -rf www_*_*
	echo "删除上一次的压缩包"

fi


 if [ ! -d "$project_path/www_${git_branch}_${git_branch_commitID}/$weixin_name" ];then
        mkdir   -p  "$project_path/www_${git_branch}_${git_branch_commitID}/$weixin_name"
	echo "$project_path/www_${git_branch}_${git_branch_commitID}/$weixin_name 创建好了 "      
 fi




#if [ ! -d "$project_path/$weixin_name" ];then
#       mkdir "$project_path/$weixin_name"
#     else
#       echo "$project_path/$weixin_name 已经创建啦"
#      echo "先将之前的文件夹删除，重新创建"
#	rm -rf $project_path/$weixin_name
#	mkdir "$project_path/$weixin_name"
#       echo "wechat 文件夹创建好了"     
# fi


cp -r  ${project_path}/web/dist  $project_path/www_${git_branch}_${git_branch_commitID}/$weixin_name

##将wechat 打gz包。
cd ${project_path}

#tar -czvf ${weixin_name}.tar.gz   ${weixin_name}
tar -czvf www_${git_branch}_${git_branch_commitID}.tar.gz www_${git_branch}_${git_branch_commitID}
