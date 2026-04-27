# FindYourCourses

Android-приложение для поиска онлайн-курсов с различных образовательных платформ (Stepik, в планах Udemy, Coursera и другие).

Разработано как pet-проект для демонстрации современных практик Android-разработки.

---

## 📱 Функциональность

- 🔍 **Поиск курсов** по ключевым словам, категориям или платформам
- 📖 **Детальная информация** о курсе: описание, рейтинг, преподаватели
- 🔖 **Избранное** — сохраняй курсы для последующего доступа
- ⚙️ **Фильтры** по цене, продолжительности, языку

---

## 🛠 Стек технологий

| Категория | Технология |
|-----------|-----------|
| **UI** | XML Layouts |
| **Network** | Retrofit |
| **DI** | ~~Dagger 2~~ → **Koin** (планируется миграция) |
| **Database** | Firebase / Room |
| **Async** | Kotlin Coroutines, Flow |
| **Architecture** | MVVM + Clean Architecture |
| **Build** | Gradle Kotlin DSL, custom build-logic |

---

## 🏗 Многомодульная архитектура

```
├── app                 # Точка входа, навигация, Application
├── build-logic         # Convention plugins
├── core
│   ├── common          # Расширения, утилиты
│   ├── network         # Retrofit, API interfaces
│   └── database        # Room, DAO
├── feature
│   ├── search          # Поиск курсов
│   ├── detail          # Детальная информация
│   └── favorites       # Избранное
└── uiKit               # Общие UI-компоненты
```

---

## 📸 Скриншоты

> _Скриншоты будут добавлены позже_

<!--
<img src="screenshots/search.png" width="300"/>
<img src="screenshots/detail.png" width="300"/>
<img src="screenshots/favorites.png" width="300"/>
-->

---

## 🚀 Установка

```bash
git clone https://github.com/lodrean/FindYourCourses.git
cd FindYourCourses
./gradlew assembleDebug
```

---

## ✅ Реализовано

- [x] Настройка навигации
- [x] Многомодульность
- [x] Поиск курсов
- [x] Экран деталей курса

## 🚧 В разработке

- [ ] Аутентификация пользователей (Firebase)
- [ ] Поисковые запросы через Ktor
- [ ] Кэширование данных (offline-first)
- [ ] Миграция на Jetpack Compose
- [ ] Unit/UI тесты

---

## 📄 Лицензия

MIT License
