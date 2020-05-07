# GoogleSpreadSheet
Android 對 Google SpreadSheet 做讀取的動作，


當APP與電腦使用GOOGLE SpreadSheet當線上資料庫，就可以做簡單的IOT監控與控制了
首先，先到Google SpreadSheet建立新文件，並且將文件設為半公開(連結可編輯或檢視)



取得網址SpreadSheet ID
  
    https://docs.google.com/spreadsheets/d/ID/XXX


取得Json檔，這邊主要做檢視用，可以先研究，等等程式就是由這邊作分析取得資料

    https://spreadsheets.google.com/tq?key=ID



可以由Json看到，一開始分為 "cols" 和 "rows"，Farmer使用rows抓取資料，
所以Farmer會大約介紹一下rows的格子規則(Farmer自想，有錯誤請指教)，
在rows中每rows為"c"開頭，每一小格中則為"v"開頭，後面則為小格中的內容
則內容皆為utf-8，所以開啟Json時，中文為亂碼是因為編碼不對的問題。

          /**
            {
                "version": "0.6",
                "reqId": "0",
                "status": "ok",
                "sig": "572817344",
                "table": {
                    "cols": [{"id":"A","label":"2019.06.12","type":"string"},{"id":"B","label":"","type":"number","pattern":"General"}],
                    "rows": ["c":[{"v":"20190613"},{"v":"Momo 電磁爐退款"}]],
                    "parsedNumHeaders": 1
                }
            }
             */
