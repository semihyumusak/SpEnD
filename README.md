# SpEnD
This respository contains the source code for the SPEC software, which is a linked data metacrawler and analyzer.
The main Web page for the project: http://wis.etu.edu.tr/spend
# Installation
- download or clone project
- if you donwload, you may open in NetBeans (File->Import Project->From ZIP->Browse->Import)
- if you clone, you may click 'Team->Git->Clone' in NetBeans and after cloning, you will extract lib.zip folder in directory of project folder
- for database, open MySql Workbench
- create schema in name of 'crawler'
- click 'Server->Data Import' and select directory of sql folder(Dump20160603-crawlernodata)
- start import
- after import, change information in db.conf according to your mysql connection information (db.conf is in SpEnD project folder)
- in addition, you may also change mysql connection information after run project (on top of opened frame 'Config->Database Connection')
- 'Build' project in NetBeans
- run project