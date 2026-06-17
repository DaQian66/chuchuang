Set WshShell = CreateObject("WScript.Shell")

' 用户端
WshShell.Run "cmd /k cd /d D:\实验8\wardrobe_user && npm run dev", 1, False

' 管理端
WshShell.Run "cmd /k cd /d D:\实验8\wardrobe_admin && npm run dev", 1, False

Set WshShell = Nothing
