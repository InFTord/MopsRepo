**Конвенция для пакетов и модулей** по типу `ml.mopspvps`
- Если одно (короткое) слово или какое-то сокращение - то добавляем `mops`
> `ml.mopspvps`
- Если два слова - то оставляем так
> `ml.woolbattle`
- Если больше двух слов - разъединяем через `-`
> `ml.website-base-framework`

**Конвенция для суб-модулей, конфигов, одиночных файлов** (которые не джава код) **и ресурсов** по типу `ml.mopsexception.configs` или `resources/configs/lang.yml` или `plugin-pvp.yaml`)
- Если одно слово или сокращение - оставляем как есть
> `.config`, `json/`
- Если несколько слов - отделяем через `-`
> `main-config.yaml`, `ru-RU.yaml`
- *Если в названии референс на что-то из первой конвенции, то пишем это так как писалось в первой конвенции*:
> `plugin-woolbattle.yml`, `config-mopspvps.json`, `mopspvps.jar`

**Java-код**
- Пишем как нормальные люди, но *с табами вместо пробелов*

**YAML, .github, pom.xml... коды**
- Пишем как нормальные люди что бы ничего не сломать

**Разделение слов**
- Слова как gameplay, scoreboard и т.д. могут считаться и как 1 слово и как 2, а так же в конкретных случаев при их использовании можно нарушать конвенцию ради читабельности:
> вместо **нечитабельного** `ml.gameplayelement` лучше писать `ml.gameplay-element`
> **НО** при этом **не надо** писать `ml.mopspvps.mopsgameplay` или `cfgs/mopsgameplay.yml`
