-- Huaxia Architecture Atlas - Seed data (Docker init)
SET NAMES utf8mb4;
USE huaxia_atlas;
-- =========================
-- Seed Buildings (NO temples/pagodas)
-- =========================
INSERT INTO buildings (
        name,
        dynasty,
        location,
        type,
        year_built,
        description,
        tags,
        cover_image
    )
VALUES -- Your existing 8 (kept as-is)
    (
        'Zhaozhou Bridge',
        'Sui',
        'Zhao County, Hebei',
        'Bridge',
        'c. 605',
        'Stone segmental arch bridge; major achievement in early bridge engineering.',
        'stone,arch,bridge,engineering,hebei',
        NULL
    ),
    (
        'Forbidden City (Palace Complex)',
        'Ming',
        'Beijing',
        'Palace',
        '1406-1420',
        'Imperial palace complex with axial planning, courtyards, and timber structures.',
        'palace,courtyard,timber,imperial,beijing',
        NULL
    ),
    (
        'Siheyuan Courtyard Residence (Typical)',
        'Qing',
        'Beijing',
        'Residence',
        '18th-19th century',
        'Traditional courtyard house emphasizing symmetry, hierarchy, and enclosed space.',
        'residence,courtyard,vernacular,beijing,qing',
        NULL
    ),
    (
        'Hutong Street Layout (Historic)',
        'Ming',
        'Beijing',
        'Urban Fabric',
        '15th-19th century',
        'Lane-based urban pattern supporting courtyard housing and neighborhood life.',
        'urban,streets,housing,courtyard,beijing',
        NULL
    ),
    (
        'Ancient City Wall (Representative Section)',
        'Ming',
        'Xi''an, Shaanxi',
        'Fortification',
        '14th century',
        'Large-scale defensive wall system with gates and watchtowers; masonry construction.',
        'wall,fortification,masonry,urban,shaanxi',
        NULL
    ),
    (
        'Traditional Yamen (Local Government Office)',
        'Qing',
        'Northern China (Representative)',
        'Office',
        '17th-19th century',
        'Local administrative office compound; formal halls and courtyard sequence.',
        'government,office,courtyard,administration,qing',
        NULL
    ),
    (
        'Classical Garden Residence (Scholars)',
        'Ming',
        'Suzhou, Jiangsu',
        'Residence',
        '16th-17th century',
        'Residential garden composition with pavilions, rocks, and water features (non-religious).',
        'garden,residence,landscape,jiangsu,ming',
        NULL
    ),
    (
        'Academy Courtyard (Study Hall)',
        'Song',
        'Southern China (Representative)',
        'Academy',
        '11th-13th century',
        'Educational courtyard complex used for teaching and scholarship.',
        'education,academy,courtyard,song,scholarship',
        NULL
    ),
    -- 92 additional named entries (no "Representative" in the name)
    (
        'Marco Polo Bridge (Lugou Bridge)',
        'Jin',
        'Fengtai District, Beijing',
        'Bridge',
        '1189-1192',
        'Stone bridge over the Yongding River, famed for carved stone lions and historic transport significance.',
        'bridge,stone,masonry,engineering,beijing',
        NULL
    ),
    (
        'Guangji Bridge (Xiangzi Bridge)',
        'Song',
        'Chaozhou, Guangdong',
        'Bridge',
        '1170-1206',
        'Bridge complex combining stone piers and movable floating sections; repeatedly rebuilt and maintained.',
        'bridge,stone,engineering,water,guangdong',
        NULL
    ),
    (
        'Luoyang Bridge (Wan''an Bridge)',
        'Song',
        'Quanzhou, Fujian',
        'Bridge',
        '1053-1059',
        'Early large-scale stone beam bridge using raft foundation techniques in a tidal estuary environment.',
        'bridge,stone,engineering,water,fujian',
        NULL
    ),
    (
        'Anping Bridge (Wuli Bridge)',
        'Song',
        'Jinjiang, Quanzhou, Fujian',
        'Bridge',
        '1138-1151',
        'Long stone beam bridge supporting coastal transport and commerce in the Quanzhou region.',
        'bridge,stone,masonry,engineering,fujian',
        NULL
    ),
    (
        'Baodai Bridge',
        'Tang (rebuilt later)',
        'Suzhou, Jiangsu',
        'Bridge',
        '816 (rebuilt 15th century)',
        'Multi-arch stone bridge spanning canal waters; a landmark of Jiangnan bridge building traditions.',
        'bridge,stone,masonry,water,jiangsu',
        NULL
    ),
    (
        'Chengyang Wind and Rain Bridge',
        'Republic era',
        'Sanjiang, Guangxi',
        'Bridge',
        '1912-1916',
        'Covered timber bridge serving both crossing and community shelter; notable carpentry and truss logic.',
        'bridge,timber,vernacular,craft,guangxi',
        NULL
    ),
    (
        'Wuhan Yangtze River Bridge',
        'Modern',
        'Wuhan, Hubei',
        'Bridge',
        '1955-1957',
        'First major bridge spanning the Yangtze at Wuhan; integrated rail and roadway deck.',
        'bridge,infrastructure,engineering,transport,hubei',
        NULL
    ),
    (
        'Nanjing Yangtze River Bridge',
        'Modern',
        'Nanjing, Jiangsu',
        'Bridge',
        '1960-1968',
        'Large river crossing combining rail and roadway; iconic modern infrastructure landmark.',
        'bridge,infrastructure,engineering,transport,jiangsu',
        NULL
    ),
    (
        'Dujiangyan Irrigation System',
        'Qin',
        'Dujiangyan, Sichuan',
        'Infrastructure',
        'c. 256 BCE',
        'Irrigation and flood-control works on the Min River, widely regarded as a major hydraulic engineering achievement.',
        'infrastructure,water,engineering,irrigation,sichuan',
        NULL
    ),
    (
        'Lingqu Canal',
        'Qin',
        'Guilin, Guangxi',
        'Infrastructure',
        '214 BCE',
        'Canal linking river systems to support transport and logistics; early example of large-scale water engineering.',
        'infrastructure,canal,water,engineering,guangxi',
        NULL
    ),
    (
        'Beijing-Hangzhou Grand Canal',
        'Sui (expanded later)',
        'Beijing-Hangzhou',
        'Infrastructure',
        '604-609 (expanded later)',
        'Major canal network linking north and south; foundational transport corridor for administration and trade.',
        'infrastructure,canal,transport,engineering,water',
        NULL
    ),
    (
        'Zhengguo Canal',
        'Qin',
        'Jingyang, Shaanxi',
        'Infrastructure',
        '246-236 BCE',
        'Irrigation canal system improving agricultural productivity in the Guanzhong Plain.',
        'infrastructure,water,engineering,irrigation,shaanxi',
        NULL
    ),
    (
        'Turpan Karez (Kanh Erjing) Wells System',
        'Qing (in-use earlier)',
        'Turpan, Xinjiang',
        'Infrastructure',
        '18th-19th century (in-use earlier)',
        'Underground water channels and shafts enabling irrigation and settlement in arid environments.',
        'infrastructure,water,engineering,irrigation,xinjiang',
        NULL
    ),
    (
        'Shanhai Pass (Shanhaiguan)',
        'Ming',
        'Qinhuangdao, Hebei',
        'Fortification',
        '1381',
        'Strategic pass where fortifications control the corridor between mountains and sea.',
        'fortification,wall,gate,masonry,hebei',
        NULL
    ),
    (
        'Jiayu Pass (Jiayuguan)',
        'Ming',
        'Jiayuguan, Gansu',
        'Fortification',
        'c. 1372',
        'Fortress-pass anchoring the western end of Ming frontier defenses in the Hexi Corridor.',
        'fortification,wall,pass,masonry,gansu',
        NULL
    ),
    (
        'Juyong Pass (Juyongguan)',
        'Ming',
        'Changping District, Beijing',
        'Fortification',
        '14th-16th century',
        'Mountain pass fortified to secure access routes to the capital; layered gate and wall works.',
        'fortification,wall,pass,masonry,beijing',
        NULL
    ),
    (
        'Badaling Great Wall',
        'Ming',
        'Yanqing District, Beijing',
        'Fortification',
        '16th century',
        'Highly developed wall section with towers and parapets; representative of late Ming defensive works.',
        'fortification,wall,tower,masonry,beijing',
        NULL
    ),
    (
        'Mutianyu Great Wall',
        'Ming',
        'Huairou District, Beijing',
        'Fortification',
        'c. 1404',
        'Well-preserved wall section with dense watchtowers; key example of Ming frontier engineering.',
        'fortification,wall,tower,masonry,beijing',
        NULL
    ),
    (
        'City Wall of Nanjing',
        'Ming',
        'Nanjing, Jiangsu',
        'Fortification',
        '1366-1386',
        'Massive city wall system built in early Ming; notable for scale and surviving gate complexes.',
        'wall,fortification,masonry,urban,jiangsu',
        NULL
    ),
    (
        'Pingyao City Wall',
        'Ming',
        'Pingyao, Shanxi',
        'Fortification',
        'c. 1370',
        'Walled perimeter protecting a county city; integrated gates and tower structures.',
        'wall,fortification,masonry,urban,shanxi',
        NULL
    ),
    (
        'Datong City Wall',
        'Ming',
        'Datong, Shanxi',
        'Fortification',
        '14th century',
        'Fortified urban perimeter reflecting frontier pressures and military logistics of the region.',
        'wall,fortification,masonry,urban,shanxi',
        NULL
    ),
    (
        'Kaifeng City Wall',
        'Song (rebuilt later)',
        'Kaifeng, Henan',
        'Fortification',
        '10th-12th century (rebuilt later)',
        'Urban defensive wall system historically adapted to flooding and warfare; multiple rebuild phases.',
        'wall,fortification,urban,henan,song',
        NULL
    ),
    (
        'Humen Forts',
        'Qing',
        'Dongguan, Guangdong',
        'Fortification',
        '18th-19th century',
        'Coastal artillery fortifications controlling river and sea approaches near the Pearl River Delta.',
        'fortification,coastal,military,engineering,guangdong',
        NULL
    ),
    (
        'Dagu Forts',
        'Ming (expanded later)',
        'Tianjin',
        'Fortification',
        '16th century (expanded 19th)',
        'Coastal defense works guarding approaches to Tianjin and the capital region.',
        'fortification,coastal,military,engineering,tianjin',
        NULL
    ),
    (
        'Pingyao Ancient City',
        'Ming-Qing',
        'Pingyao, Shanxi',
        'Urban Fabric',
        '14th-19th century',
        'Walled city with preserved streets and courtyard compounds reflecting late-imperial urban life.',
        'urban,streets,courtyard,commerce,shanxi',
        NULL
    ),
    (
        'Lijiang Old Town',
        'Yuan-Ming-Qing',
        'Lijiang, Yunnan',
        'Urban Fabric',
        '13th-19th century',
        'Mountain town fabric integrating waterways, lanes, and courtyards; long-lived regional urban pattern.',
        'urban,streets,water,vernacular,yunnan',
        NULL
    ),
    (
        'Zhouzhuang Ancient Town',
        'Song-Qing',
        'Kunshan, Jiangsu',
        'Urban Fabric',
        '11th-19th century',
        'Canal-town street and bridge network supporting dense pedestrian commerce and waterside housing.',
        'urban,streets,water,commerce,jiangsu',
        NULL
    ),
    (
        'Tongli Ancient Town',
        'Song-Qing',
        'Suzhou, Jiangsu',
        'Urban Fabric',
        '11th-19th century',
        'Water-town urban form with canals, bridges, and compact neighborhood lanes.',
        'urban,streets,water,commerce,jiangsu',
        NULL
    ),
    (
        'Wuzhen Ancient Town',
        'Ming-Qing',
        'Tongxiang, Zhejiang',
        'Urban Fabric',
        '15th-19th century',
        'Canal-town fabric with waterside streets and traditional commercial frontage.',
        'urban,streets,water,commerce,zhejiang',
        NULL
    ),
    (
        'Fenghuang Ancient Town',
        'Qing',
        'Xiangxi, Hunan',
        'Urban Fabric',
        'c. 1704',
        'Riverfront townscape with stilted houses and narrow lanes supporting local trade.',
        'urban,streets,water,vernacular,hunan',
        NULL
    ),
    (
        'Kashgar Old City',
        'Ming-Qing',
        'Kashgar, Xinjiang',
        'Urban Fabric',
        '16th-19th century',
        'Historic neighborhood fabric adapted to arid climate with dense lanes and courtyard compounds.',
        'urban,streets,courtyard,vernacular,xinjiang',
        NULL
    ),
    (
        'Shamian Island Historic District',
        'Late Qing-Modern',
        'Guangzhou, Guangdong',
        'Urban Fabric',
        '19th-early 20th century',
        'Planned river-island district of civic and commercial buildings reflecting treaty-port era urbanism.',
        'urban,civic,commerce,modern,guangdong',
        NULL
    ),
    (
        'Five Great Avenues (Wudadao)',
        'Modern',
        'Tianjin',
        'Urban Fabric',
        '1900s-1930s',
        'Planned residential district featuring early modern streetscape, villas, and civic frontage.',
        'urban,streets,residence,modern,tianjin',
        NULL
    ),
    (
        'Former French Concession (Historic Streetscape)',
        'Modern',
        'Shanghai',
        'Urban Fabric',
        '1849-1943',
        'Tree-lined street network and dense blocks reflecting early modern planning and mixed-use development.',
        'urban,streets,modern,commerce,shanghai',
        NULL
    ),
    (
        'Daming Palace (Ruins Park)',
        'Tang',
        'Xi''an, Shaanxi',
        'Palace',
        '634',
        'Imperial palace precinct ruins representing Tang-period capital planning and monumental state architecture.',
        'palace,urban,ruins,imperial,shaanxi',
        NULL
    ),
    (
        'Weiyang Palace (Ruins)',
        'Han',
        'Xi''an, Shaanxi',
        'Palace',
        'c. 200 BCE',
        'Ruins of a major Western Han palace precinct; large-scale early imperial planning footprint.',
        'palace,urban,ruins,imperial,shaanxi',
        NULL
    ),
    (
        'Mukden Palace (Shenyang Imperial Palace)',
        'Later Jin-Qing',
        'Shenyang, Liaoning',
        'Palace',
        '1625-1636',
        'Imperial palace complex reflecting early Qing court architecture and regional influences.',
        'palace,courtyard,timber,imperial,liaoning',
        NULL
    ),
    (
        'Summer Palace (Yiheyuan)',
        'Qing',
        'Beijing',
        'Palace',
        '1750 (rebuilt 1886-1895)',
        'Imperial garden-palace integrating lakes, hills, halls, and long corridors into a coherent landscape composition.',
        'palace,garden,landscape,imperial,beijing',
        NULL
    ),
    (
        'Chengde Mountain Resort',
        'Qing',
        'Chengde, Hebei',
        'Palace',
        '1703-1792',
        'Imperial resort complex combining palace precincts with expansive designed landscape and lakes.',
        'palace,garden,landscape,imperial,hebei',
        NULL
    ),
    (
        'Prince Gong''s Mansion',
        'Qing',
        'Beijing',
        'Residence',
        '1776 (expanded later)',
        'Large elite courtyard residence with garden spaces reflecting late-imperial residential hierarchy.',
        'residence,courtyard,garden,elite,beijing',
        NULL
    ),
    (
        'Qiao Family Compound',
        'Qing',
        'Qi County, Shanxi',
        'Residence',
        '1756-19th century',
        'Merchant family compound with layered courtyards, gate control, and strong spatial hierarchy.',
        'residence,courtyard,compound,commerce,shanxi',
        NULL
    ),
    (
        'Wang Family Compound',
        'Qing',
        'Lingshi, Shanxi',
        'Residence',
        '18th-19th century',
        'Extensive merchant-family courtyard cluster; complex circulation and hierarchical planning.',
        'residence,courtyard,compound,commerce,shanxi',
        NULL
    ),
    (
        'Hu Xueyan Residence',
        'Qing',
        'Hangzhou, Zhejiang',
        'Residence',
        'c. 1872',
        'Merchant-official residence integrating courtyards, halls, and garden elements.',
        'residence,courtyard,garden,commerce,zhejiang',
        NULL
    ),
    (
        'Chen Clan Ancestral Hall (Chen Clan Academy)',
        'Late Qing',
        'Guangzhou, Guangdong',
        'Civic',
        '1890-1894',
        'Clan-funded civic and educational complex with richly crafted timber halls and courtyards.',
        'civic,education,timber,courtyard,guangdong',
        NULL
    ),
    (
        'Tianyi Pavilion',
        'Ming',
        'Ningbo, Zhejiang',
        'Academy',
        '1561',
        'Historic private library compound emphasizing controlled access, courtyards, and archival storage logic.',
        'education,library,courtyard,ming,zhejiang',
        NULL
    ),
    (
        'Yuelu Academy',
        'Song',
        'Changsha, Hunan',
        'Academy',
        '976',
        'Major academy complex with axial courtyard sequence supporting scholarship and teaching.',
        'education,academy,courtyard,song,hunan',
        NULL
    ),
    (
        'White Deer Grotto Academy',
        'Tang-Song',
        'Jiujiang, Jiangxi',
        'Academy',
        '940 (rebuilt 1179)',
        'Historic academy precinct associated with Neo-Confucian learning; formal halls and courtyard order.',
        'education,academy,courtyard,jiangxi',
        NULL
    ),
    (
        'Songyang Academy',
        'Song',
        'Dengfeng, Henan',
        'Academy',
        '11th century',
        'Academy compound supporting teaching and scholarship; courtyard-based institutional form.',
        'education,academy,courtyard,henan',
        NULL
    ),
    (
        'Donglin Academy',
        'Song-Ming',
        'Wuxi, Jiangsu',
        'Academy',
        '1111 (rebuilt 1604)',
        'Academy and lecture-hall complex linked to intellectual movements; formal halls with courtyards.',
        'education,academy,courtyard,jiangsu',
        NULL
    ),
    (
        'Imperial College (Guozijian)',
        'Yuan',
        'Beijing',
        'Academy',
        '1306',
        'State education institution with ceremonial and teaching spaces organized along formal courtyards.',
        'education,academy,courtyard,beijing',
        NULL
    ),
    (
        'Jiangnan Examination Hall (Jiangnan Gongyuan)',
        'Song-Qing',
        'Nanjing, Jiangsu',
        'Academy',
        '1168 (expanded later)',
        'Examination compound supporting civil service testing; regimented spaces for large-scale administration.',
        'education,examination,institution,jiangsu',
        NULL
    ),
    (
        'Humble Administrator''s Garden',
        'Ming',
        'Suzhou, Jiangsu',
        'Residence',
        'c. 1509',
        'Classical garden composition integrating water, rocks, and pavilions within an urban estate setting.',
        'garden,residence,landscape,jiangsu,ming',
        NULL
    ),
    (
        'Lingering Garden',
        'Ming',
        'Suzhou, Jiangsu',
        'Residence',
        'c. 1593',
        'Garden estate integrating halls, rockeries, and controlled views; refined spatial sequencing.',
        'garden,residence,landscape,jiangsu,ming',
        NULL
    ),
    (
        'Master of the Nets Garden',
        'Song (rebuilt Qing)',
        'Suzhou, Jiangsu',
        'Residence',
        '12th century (rebuilt 18th)',
        'Compact garden residence emphasizing layered courtyards and carefully framed interior landscapes.',
        'garden,residence,landscape,jiangsu',
        NULL
    ),
    (
        'Lion Grove Garden',
        'Yuan',
        'Suzhou, Jiangsu',
        'Residence',
        '1342',
        'Garden known for rockery composition and spatial depth within a compact footprint.',
        'garden,residence,landscape,jiangsu,yuan',
        NULL
    ),
    (
        'Couple''s Retreat Garden',
        'Qing',
        'Suzhou, Jiangsu',
        'Residence',
        'c. 1874',
        'Late-imperial garden residence balancing water, halls, and circulation in a tight urban lot.',
        'garden,residence,landscape,jiangsu,qing',
        NULL
    ),
    (
        'Beihai Park (Imperial Garden Precinct)',
        'Liao-Qing',
        'Beijing',
        'Civic',
        '10th-18th century',
        'Imperial garden precinct developed over centuries; lakeside pavilions and axial links shaping public landscape today.',
        'civic,garden,landscape,beijing',
        NULL
    ),
    (
        'Rishengchang Draft Bank',
        'Qing',
        'Pingyao, Shanxi',
        'Commercial',
        '1823',
        'Historic financial institution compound with storefront and courtyard office spaces supporting remittance operations.',
        'commerce,finance,courtyard,shanxi,qing',
        NULL
    ),
    (
        'Qinghefang Ancient Street',
        'Song-Qing',
        'Hangzhou, Zhejiang',
        'Commercial',
        '12th-19th century',
        'Historic commercial street with shopfront continuity and dense pedestrian-scale trading fabric.',
        'commerce,streets,urban,zhejiang',
        NULL
    ),
    (
        'Shantang Street',
        'Tang',
        'Suzhou, Jiangsu',
        'Commercial',
        '825 (expanded later)',
        'Canal-adjacent commercial street supporting trade, lodging, and pedestrian movement.',
        'commerce,streets,water,jiangsu',
        NULL
    ),
    (
        'Sideng Street (Shaxi)',
        'Ming-Qing',
        'Jianchuan, Yunnan',
        'Commercial',
        '14th-19th century',
        'Market street node on historic trade routes; compact blocks with mixed commercial and residential frontage.',
        'commerce,streets,urban,yunnan',
        NULL
    ),
    (
        'Dashilan Commercial Street',
        'Ming-Qing',
        'Beijing',
        'Commercial',
        '15th-19th century',
        'Historic retail street corridor with dense shopfronts and courtyards behind street facades.',
        'commerce,streets,urban,beijing',
        NULL
    ),
    (
        'Pingyao County Government Office',
        'Ming-Qing',
        'Pingyao, Shanxi',
        'Office',
        '14th-19th century',
        'County yamen compound integrating gate-hall sequence, courtyards, and administrative rooms.',
        'government,office,courtyard,administration,shanxi',
        NULL
    ),
    (
        'Nanjing Presidential Palace',
        'Republic era',
        'Nanjing, Jiangsu',
        'Office',
        '1912-1949 (site older)',
        'Government office compound representing early modern administrative architecture and layered site history.',
        'government,office,modern,jiangsu',
        NULL
    ),
    (
        'Zhifu Yamen',
        'Qing',
        'Yantai, Shandong',
        'Office',
        '18th-19th century',
        'Prefectural government office compound with formal halls and courtyard sequences for administration.',
        'government,office,courtyard,shandong,qing',
        NULL
    ),
    (
        'Huguang Guild Hall',
        'Qing',
        'Chongqing',
        'Commercial',
        '1759 (rebuilt 1840s)',
        'Guild hall complex supporting merchant networks, lodging, and assembly; courtyard layout with stage spaces.',
        'commerce,guildhall,courtyard,civic,chongqing',
        NULL
    ),
    (
        'Tea Horse Road (Ancient Trade Route)',
        'Tang-Qing',
        'Yunnan-Sichuan-Tibet Region',
        'Transport',
        '7th-19th century',
        'Overland trade route network supporting tea and horse exchange; route-side towns and logistics nodes.',
        'transport,trade,infrastructure,route,yunnan',
        NULL
    ),
    (
        'Shudao Plank Road (Historic Route Segments)',
        'Qin-Han',
        'Shaanxi-Sichuan Corridor',
        'Transport',
        '3rd century BCE-1st century',
        'Mountain transport routes using cliff-side road engineering to link basins and passes.',
        'transport,infrastructure,engineering,mountains,sichuan',
        NULL
    ),
    (
        'Jiaohe Ruins',
        'Han-Yuan',
        'Turpan, Xinjiang',
        'Urban Fabric',
        '2nd century BCE-14th century',
        'Ancient earthen city site with street network and dense blocks adapted to arid plateau conditions.',
        'urban,ruins,earth,streets,xinjiang',
        NULL
    ),
    (
        'Gaochang Ruins',
        'Han-Yuan',
        'Turpan, Xinjiang',
        'Urban Fabric',
        '1st century BCE-14th century',
        'Ruined city site with administrative precinct traces and block structure in an oasis environment.',
        'urban,ruins,earth,streets,xinjiang',
        NULL
    ),
    (
        'Hongcun Village',
        'Song-Qing',
        'Yixian, Anhui',
        'Urban Fabric',
        'c. 1131-19th century',
        'Clan-based village fabric with lanes, ponds, and Huizhou-style residential compounds.',
        'urban,village,courtyard,vernacular,anhui',
        NULL
    ),
    (
        'Xidi Village',
        'Song-Qing',
        'Yixian, Anhui',
        'Urban Fabric',
        'c. 1047-19th century',
        'Huizhou village fabric with dense lanes and courtyard houses reflecting merchant-era prosperity.',
        'urban,village,courtyard,vernacular,anhui',
        NULL
    ),
    (
        'Dangjia Village',
        'Yuan-Qing',
        'Hancheng, Shaanxi',
        'Urban Fabric',
        'c. 1315-19th century',
        'Walled village fabric with lane hierarchy and courtyard residences adapted to loess terrain.',
        'urban,village,courtyard,vernacular,shaanxi',
        NULL
    ),
    (
        'Likeng Village (Wuyuan)',
        'Ming-Qing',
        'Wuyuan, Jiangxi',
        'Urban Fabric',
        '15th-19th century',
        'Village lanes and waterways with traditional residences reflecting Huizhou cultural influence.',
        'urban,village,water,vernacular,jiangxi',
        NULL
    ),
    (
        'Chengqi Lou (Fujian Tulou)',
        'Qing',
        'Yongding, Fujian',
        'Residence',
        '1709',
        'Large circular earthen communal residence organized around a central courtyard and ceremonial core.',
        'residence,rammed-earth,communal,courtyard,fujian',
        NULL
    ),
    (
        'Zhencheng Lou (Fujian Tulou)',
        'Republic era',
        'Yongding, Fujian',
        'Residence',
        '1912',
        'Earthen communal residence combining traditional form with early modern construction refinements.',
        'residence,rammed-earth,communal,courtyard,fujian',
        NULL
    ),
    (
        'Tianluokeng Tulou Cluster',
        'Qing',
        'Nanjing County, Fujian',
        'Residence',
        '18th century',
        'Cluster of communal earthen residences arranged to suit topography and lineage organization.',
        'residence,rammed-earth,communal,cluster,fujian',
        NULL
    ),
    (
        'Ruishi Lou (Kaiping Diaolou)',
        'Republic era',
        'Jinjiangli Village, Kaiping, Guangdong',
        'Tower',
        '1920s',
        'Nine-storey fortified tower residence blending local and imported design features for security and status.',
        'tower,residence,defense,hybrid,guangdong',
        NULL
    ),
    (
        'Majianglong Diaolou Cluster',
        'Republic era',
        'Kaiping, Guangdong',
        'Tower',
        '1920s-1930s',
        'Landscape of fortified tower houses set among fields and waterways; security-driven rural architecture.',
        'tower,defense,vernacular,cluster,guangdong',
        NULL
    ),
    (
        'Zili Village Diaolou Cluster',
        'Republic era',
        'Kaiping, Guangdong',
        'Tower',
        '1920s-1930s',
        'Village group of fortified towers reflecting overseas-influenced styles and local security needs.',
        'tower,defense,vernacular,cluster,guangdong',
        NULL
    ),
    (
        'Sun Yat-sen Mausoleum',
        'Republic era',
        'Nanjing, Jiangsu',
        'Civic',
        '1926-1929',
        'Ceremonial memorial complex emphasizing axial procession and monumental stair sequence.',
        'civic,memorial,axial,monument,jiangsu',
        NULL
    ),
    (
        'Sun Yat-sen Memorial Hall (Guangzhou)',
        'Republic era',
        'Guangzhou, Guangdong',
        'Civic',
        '1931',
        'Large public assembly hall and memorial building; civic landmark of early modern era.',
        'civic,memorial,assembly,modern,guangdong',
        NULL
    ),
    (
        'The Bund (Historic Waterfront Streetscape)',
        'Modern',
        'Shanghai',
        'Urban Fabric',
        '1840s-1930s',
        'Historic waterfront streetscape with dense commercial and civic frontage reflecting early modern urban development.',
        'urban,streets,modern,commerce,shanghai',
        NULL
    ),
    (
        'Shanghai Customs House',
        'Modern',
        'Shanghai',
        'Office',
        '1925-1927',
        'Major civic-office building associated with port administration and trade-era governance.',
        'government,office,modern,commerce,shanghai',
        NULL
    ),
    (
        'Beijing Railway Station',
        'Modern',
        'Beijing',
        'Transport',
        '1959',
        'Major passenger rail station integrating large hall volumes and transport concourses.',
        'transport,infrastructure,modern,beijing',
        NULL
    ),
    (
        'Central Street (Zhongyang Dajie)',
        'Modern',
        'Harbin, Heilongjiang',
        'Urban Fabric',
        '1898-1930s',
        'Historic pedestrian commercial street corridor with early modern block frontage and civic fabric.',
        'urban,streets,modern,commerce,heilongjiang',
        NULL
    ),
    (
        'Gulangyu Historic International Settlement',
        'Late Qing-Modern',
        'Xiamen, Fujian',
        'Urban Fabric',
        'late 19th-early 20th century',
        'Compact island district with early modern streetscape and civic-commercial building fabric.',
        'urban,modern,civic,commerce,fujian',
        NULL
    ),
    (
        'Ming Xiaoling Mausoleum (Site Complex)',
        'Ming',
        'Nanjing, Jiangsu',
        'Civic',
        '1381-1405',
        'Imperial mausoleum complex emphasizing processional planning and monumental stone setting.',
        'civic,imperial,memorial,axial,jiangsu',
        NULL
    );
-- =========================
-- Seed Posts
-- =========================
INSERT INTO posts (
        title,
        content,
        author_name,
        author_email,
        status
    )
VALUES (
        'What makes timber-frame architecture durable?',
        'Timber structures can last centuries when protected from moisture and designed with proper joinery. Share examples from historical sites.',
        'Abdullah',
        'abdullah@example.com',
        'APPROVED'
    ),
    (
        'I found a historic courtyard house in my city',
        'I want to share photos and a short history. Please review and approve my post for the community section.',
        'Student User',
        'student@example.com',
        'PENDING'
    );
-- =========================
-- Seed Messages (Contact → Admin inbox)
-- =========================
INSERT INTO messages (name, email, subject, message, is_read)
VALUES (
        'Visitor',
        'visitor@example.com',
        'Question about sources',
        'Can you share references for the palace layout drawings used in the site?',
        0
    );
-- =========================
-- Seed AI logs (optional)
-- =========================
INSERT INTO search_logs (query_text)
VALUES ('bridge stone arch'),
    ('Ming palace courtyard'),
    ('Qing residence siheyuan');
INSERT INTO chat_logs (user_question, model_answer)
VALUES (
        'Explain what a siheyuan is.',
        'A siheyuan is a traditional courtyard residence organized around an inner courtyard, reflecting hierarchy and privacy.'
    );
-- =========================
-- Seed Products
-- =========================
INSERT INTO products (name, description, price, image_url, stock)
VALUES (
        'Atlas Field Guide',
        'Compact guide to courtyard and palace layouts.',
        29.00,
        NULL,
        12
    ),
    (
        'Courtyard Sketch Set',
        'Study sketches and site notes for historic compounds.',
        18.50,
        NULL,
        8
    ),
    (
        'Architectural Map Print',
        'Large format print of key pre-1911 sites.',
        42.00,
        NULL,
        5
    );