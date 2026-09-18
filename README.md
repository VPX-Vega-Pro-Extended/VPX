<div align="center">

# ⚡ VPX — Vega Pro Extended

### ایجنت هوش مصنوعی قدرتمند، قابل‌کنترل و توسعه‌پذیر برای اندروید

`مدیریت فایل` · `تحقیق و مرور وب` · `ویرایش کد` · `اجرای وظایف چندمرحله‌ای` · `Self-Repair` · `Voice Trigger` · `Floating Interaction`

**Local-First · User-Controlled · BYOK · Provider-Flexible**

<br>

[![Android](https://img.shields.io/badge/Android-6.0%2B-3DDC84?style=for-the-badge\&logo=android\&logoColor=white\&labelColor=1B1F23)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge\&logo=kotlin\&logoColor=white\&labelColor=1B1F23)](https://kotlinlang.org/)
[![Local First](https://img.shields.io/badge/Local--First-00C853?style=for-the-badge\&logo=shieldsdotio\&logoColor=white\&labelColor=1B1F23)](#security--privacy)
[![AGPL-3.0](https://img.shields.io/badge/License-AGPL--3.0-2962FF?style=for-the-badge\&logo=gnu\&logoColor=white\&labelColor=1B1F23)](LICENSE)

<br>

**[🇮🇷 فارسی](#-فارسی)** · **[🇬🇧 English](#-english)** · [🗺️ فهرست مطالب](#-فهرست-مطالب)

</div>

---

# 📌 درباره پروژه

**VPX — Vega Pro Extended** یک پروژه مستقل و جامعه‌محور برای ارائه یک **AI Agent قابل‌کنترل روی Android** است.

VPX با هدف ترکیب مدل‌های ابری و محلی با یک Runtime ابزارمحور طراحی شده است تا مدل هوش مصنوعی صرفاً یک رابط گفت‌وگو نباشد، بلکه بتواند با کنترل کاربر:

* روی فایل‌ها و پروژه‌ها کار کند
* کد و متون را بخواند و ویرایش کند
* در وب جستجو و مرور انجام دهد
* وظایف پیچیده را به چند مرحله تقسیم کند
* ابزارهای مختلف را فراخوانی و نتیجه آن‌ها را مدیریت کند
* وضعیت اجرای عملیات را نمایش دهد
* در سناریوهای مشخص وضعیت خود را بازیابی یا تعمیر کند
* از سرویس‌های هوش مصنوعی ابری یا مدل‌های محلی استفاده کند
* تعاملات صوتی و شناور را در سطح سیستم ارائه دهد

VPX بر پایه معماری **Local-First** ساخته شده است؛ رابط کاربری، مدیریت وضعیت، Runtime ایجنت، ابزارها، تنظیمات و سیستم Diagnostics در خود دستگاه اجرا می‌شوند و برای هسته برنامه به یک سرور واسط اختصاصی نیاز نیست.

---

# ✨ قابلیت‌ها

| قابلیت                                | توضیح                                                |
| ------------------------------------- | ---------------------------------------------------- |
| 🤖 **AI Agent Runtime**               | اجرای وظایف چندمرحله‌ای و ابزارمحور                  |
| 🧩 **Task Orchestration**             | تقسیم و مدیریت وظایف پیچیده                          |
| 🛠️ **Tool System**                   | فایل، وب، پردازش، اپلیکیشن، حافظه و ابزارهای اختصاصی |
| 📂 **Filesystem Operations**          | خواندن، ایجاد، ویرایش و جستجوی فایل‌ها               |
| 🔍 **Code & Text Editing**            | ویرایش کد و متن همراه با نمایش تغییرات               |
| 🌐 **Web Research**                   | جستجو، دریافت صفحات و مرور تعاملی                    |
| 🧠 **Reasoning Control**              | کنترل سطح تلاش استدلال مدل، در صورت پشتیبانی مدل     |
| 🛡️ **Execution Modes**               | Automatic، Planning و Accepting                      |
| 🔧 **Self-Repair**                    | زیرساخت تعمیر و بازیابی خودکار پروژه                 |
| 🎙️ **Voice Trigger**                 | سرویس اختصاصی برای تعاملات صوتی                      |
| 💧 **Floating Bubble**                | رابط شناور مینیمال برای تعامل سریع                   |
| 🖥️ **Screen Control Infrastructure** | زیرساخت کنترل صفحه در حال توسعه                      |
| 📋 **Diagnostics & Logging**          | لاگ مرکزی، گزارش خطا و اشتراک‌گذاری گزارش            |
| 🔐 **Secure Storage**                 | Android Keystore و رمزنگاری داده‌های حساس            |
| 🔑 **BYOK**                           | استفاده مستقیم از کلید API کاربر                     |
| 🏠 **Local Models**                   | Ollama و LM Studio                                   |
| 🌍 **OpenAI-Compatible APIs**         | اتصال به سرویس‌های سازگار با OpenAI API              |
| 🌐 **Persian / English**              | رابط RTL/LTR و پشتیبانی دوزبانه                      |
| ⚙️ **Foreground Services**            | اجرای سرویس‌ها و وظایف طولانی در پس‌زمینه            |
| 🧪 **Regression Tests**               | تست‌های پایه برای جلوگیری از بازگشت خطاهای قبلی      |

---

# 🧠 AI Agent Runtime

هسته VPX یک Runtime ابزارمحور است که وظیفه آن هماهنگ‌کردن مدل، ابزارها، وضعیت اجرا و نتیجه عملیات است.

Agent Engine می‌تواند:

* درخواست کاربر را تحلیل کند
* وظایف پیچیده را به مراحل کوچک‌تر تقسیم کند
* ابزار مناسب را انتخاب کند
* خروجی ابزارها را دریافت و در ادامه تصمیم‌گیری استفاده کند
* وضعیت اجرای Task را مدیریت کند
* عملیات چندمرحله‌ای را دنبال کند
* نتیجه هر مرحله را در اختیار رابط کاربری قرار دهد
* در صورت نیاز از حافظه و وضعیت قبلی استفاده کند

معماری VPX طوری طراحی شده که مدل هوش مصنوعی **تصمیم‌گیرنده** باشد، اما اجرای واقعی عملیات از طریق ابزارهای کنترل‌شده انجام شود.

---

# 🛡️ Execution Modes

VPX سه حالت اصلی برای کنترل میزان استقلال Agent دارد:

| حالت             | توضیح                                      |
| ---------------- | ------------------------------------------ |
| ⚙️ **Automatic** | اجرای عملیات بدون تأیید مرحله‌به‌مرحله     |
| 🗺️ **Planning** | ابتدا تحلیل و برنامه‌ریزی، سپس اجرای مراحل |
| ✅ **Accepting**  | درخواست تأیید کاربر برای اقدامات حساس      |

این حالت‌ها برای ایجاد تعادل بین **Autonomy** و **User Control** طراحی شده‌اند.

در حالت‌های محدودکننده، ابزارهای حساس می‌توانند از اجرای مستقیم منع شوند. برای مثال، عملیات Self-Repair در مسیرهای اجرایی خاص مانند Planning به‌صورت مستقیم در اختیار Agent قرار نمی‌گیرد.

---

# 🧩 Tool System

سیستم ابزار VPX یکی از بخش‌های اصلی معماری پروژه است.

ابزارهای موجود یا زیرساخت آن‌ها شامل حوزه‌های زیر هستند:

### 📂 Filesystem

* خواندن فایل
* ایجاد فایل
* ویرایش فایل
* ایجاد و مدیریت پوشه
* جستجوی متن
* Glob/File Search
* نمایش تغییرات
* کار با کد منبع
* کار با فایل‌های متنی
* ZIP
* استخراج و پردازش PDF

### 🌐 Web

* جستجوی وب
* دریافت صفحات
* تحلیل محتوای صفحات
* مرور تعاملی
* WebView
* دسترسی کنترل‌شده به شبکه محلی
* پشتیبانی از صفحات نیازمند تعامل کاربر

### ⚙️ Process / Application

زیرساخت ابزار برای اجرای عملیات مرتبط با Process و Application نیز در Runtime پیش‌بینی شده است و سطح واقعی دسترسی آن به محدودیت‌های Android و مجوزهای سیستم وابسته است.

### 🧠 Memory

سیستم Memory برای نگهداری اطلاعات موردنیاز Agent در جریان کار طراحی شده است.

اطلاعات حساس، از جمله داده‌هایی که نباید در حافظه Agent باقی بمانند، باید قبل از ذخیره‌سازی مناسب‌سازی یا Redact شوند.

### 🔧 Task

سیستم Task امکان اجرای جریان‌های کاری چندمرحله‌ای و مدیریت وضعیت عملیات را فراهم می‌کند.

### 🛠️ Self-Repair

Self-Repair به‌عنوان یک ابزار/زیرسیستم اختصاصی برای تشخیص و اصلاح مشکلات پروژه در نظر گرفته شده است و تحت محدودیت‌های امنیتی و Execution Mode اجرا می‌شود.

---

# 📂 Filesystem & Code Operations

VPX برای کار مستقیم با فایل‌های دستگاه طراحی شده است.

Agent می‌تواند، با توجه به سطح دسترسی و Workspace انتخاب‌شده:

* فایل‌ها را بخواند
* فایل جدید ایجاد کند
* فایل‌ها را ویرایش کند
* پوشه ایجاد یا مدیریت کند
* در فایل‌ها جستجو کند
* فایل‌ها را بر اساس الگو پیدا کند
* تغییرات را قبل از اعمال بررسی کند
* فایل‌های Source Code را ویرایش کند
* فایل‌های ZIP را پردازش کند
* محتوای PDF را استخراج و پردازش کند

> دسترسی واقعی به فایل‌ها به نسخه Android، مجوزهای اعطا‌شده و محدوده Workspace یا مسیر انتخاب‌شده توسط کاربر بستگی دارد.

---

# 🌐 Web Research & Browsing

VPX قابلیت‌های تحقیقاتی وب را در اختیار Agent قرار می‌دهد.

امکانات شامل:

* جستجوی وب
* DuckDuckGo
* Bing
* دریافت صفحات وب
* پردازش محتوای صفحات
* مرور تعاملی با Android WebView
* کنترل دسترسی به شبکه محلی
* تعامل کاربر با صفحات نیازمند Login یا Confirmation

VPX برای دورزدن مکانیزم‌های امنیتی وب‌سایت‌ها طراحی نشده است.

---

# 🤖 AI Providers

VPX از معماری Provider-Flexible استفاده می‌کند.

Providerهای پشتیبانی‌شده یا قابل اتصال شامل:

* OpenAI
* Anthropic Claude
* Google Gemini
* OpenRouter
* Microsoft Azure OpenAI
* Ollama
* LM Studio
* سرویس‌های سازگار با OpenAI API

در سرویس‌های سازگار، امکان تنظیم مواردی مانند:

```text
Base URL
API Key
Model
```

وجود دارد.

---

# 🔑 BYOK — Bring Your Own Key

VPX از مدل **BYOK** استفاده می‌کند.

یعنی کاربر کلید API ارائه‌دهنده موردنظر خود را در برنامه تنظیم می‌کند و VPX برای پردازش درخواست‌ها به یک API Gateway اختصاصی وابسته نیست.

این معماری امکان استفاده از Providerهای مختلف را بدون وابستگی به یک سرویس واحد فراهم می‌کند.

> هزینه استفاده از مدل‌های ابری توسط Provider مربوطه تعیین می‌شود.

---

# 🏠 Local AI

VPX برای استفاده از مدل‌های محلی نیز طراحی شده است.

### Ollama

امکان اتصال به Ollama روی:

* خود دستگاه
* شبکه محلی
* سیستم دیگری در شبکه

وجود دارد، مشروط به اینکه endpoint از دستگاه قابل دسترسی باشد.

### LM Studio

VPX می‌تواند به API ارائه‌شده توسط LM Studio نیز متصل شود.

این موضوع امکان استفاده از مدل‌های محلی بدون ارسال داده به سرویس ابری را فراهم می‌کند.

---

# 🧠 Reasoning & Dynamic Workflows

در مدل‌هایی که از Reasoning یا کنترل تلاش استدلال پشتیبانی می‌کنند، VPX امکان تنظیم سطح تلاش استدلال را فراهم می‌کند.

Agent همچنین می‌تواند:

* وظایف پیچیده را به مراحل کوچک‌تر تقسیم کند
* عملیات وابسته را به ترتیب اجرا کند
* عملیات مستقل را مدیریت کند
* وضعیت Task را نمایش دهد
* Tool Callها را ثبت و نمایش دهد
* خروجی هر ابزار را به مرحله بعد منتقل کند

> سطح Reasoning و قابلیت‌های اجرای موازی به مدل و Provider انتخاب‌شده وابسته است.

---

# 🔧 Self-Repair & Recovery

یکی از قابلیت‌های توسعه‌یافته VPX، زیرساخت **Self-Repair** است.

این سیستم با اجزایی مانند:

* `SelfRepair`
* `RepairSessionManager`
* Repair Session
* Workspace / Sandbox separation

برای مدیریت فرآیندهای تعمیر و بازیابی طراحی شده است.

هدف این زیرسیستم آن است که Agent بتواند در سناریوهای مشخص:

1. مشکل را شناسایی کند
2. وضعیت پروژه را بررسی کند
3. یک Session تعمیر ایجاد کند
4. تغییرات موردنیاز را پیشنهاد یا اعمال کند
5. نتیجه را بررسی کند
6. وضعیت اجرای تعمیر را مدیریت کند

Self-Repair بخشی حساس از Runtime است و به همین دلیل در کنار محدودیت‌های Workspace، Sandbox و Execution Mode قرار گرفته است.

> Self-Repair را نباید با یک سیستم کاملاً خودمختار و بدون محدودیت اشتباه گرفت؛ دسترسی آن عمداً تحت کنترل Runtime قرار دارد.

---

# 🎙️ Voice Interaction

VPX دارای زیرساخت اختصاصی برای سرویس صوتی است.

`VoiceTriggerService` برای مدیریت تعاملات صوتی و اجرای سرویس‌های مرتبط در سطح Android طراحی شده است.

این بخش با معماری Service-based برنامه هماهنگ شده و امکان توسعه تعاملات صوتی پیشرفته‌تر را فراهم می‌کند.

---

# 💧 Floating Interaction

VPX دارای یک رابط شناور مینیمال برای دسترسی سریع‌تر به Agent است.

Floating Bubble با هدف:

* دسترسی سریع
* کمترین مزاحمت بصری
* باقی‌ماندن در سطح سیستم
* تعامل سریع با Agent

طراحی شده است.

ظاهر این رابط بر پایه یک فرم **Liquid / Droplet** طراحی شده که در حالت Idle می‌تواند به یک فرم باریک در لبه صفحه جمع شود.

این بخش با `FloatingBubbleService` و `FloatingBubbleView` پیاده‌سازی شده و از Android Foreground Service برای مدیریت پایدار سرویس استفاده می‌کند.

---

# 🖥️ Screen Control

زیرساخت تعامل Agent با صفحه نمایش Android در VPX در حال توسعه است.

هدف این بخش فراهم‌کردن امکان تعامل کنترل‌شده Agent با رابط کاربری دستگاه است.

این قابلیت هنوز در مرحله توسعه قرار دارد و نباید در نسخه‌های فعلی به‌عنوان یک قابلیت کاملاً نهایی‌شده در نظر گرفته شود.

---

# 📋 Diagnostics & Logging

VPX دارای سیستم Diagnostics و Logging مرکزی است.

سیستم `VpxLogger` برای ثبت رویدادهای مهم برنامه طراحی شده است.

قابلیت‌ها شامل:

* Logcat با Tag اختصاصی `VPX`
* ذخیره Log داخلی
* ثبت Lifecycle برنامه
* ثبت خطاها
* ثبت Warningها
* ثبت رویدادهای سرویس‌ها
* ثبت Exceptionهای مدیریت‌نشده
* مشاهده Log داخل Settings
* پاک‌کردن Log
* اشتراک‌گذاری گزارش Diagnostics

فایل Log داخلی در Cache برنامه نگهداری می‌شود و برای جلوگیری از رشد بی‌نهایت، محدودیت حجم دارد.

---

# 🧾 Global Crash Logging

VPX یک Global Uncaught Exception Handler نیز دارد.

در صورت رخداد Exception مدیریت‌نشده، اطلاعات مربوط به خطا قبل از خروج فرآیند تا حد امکان در سیستم Diagnostics ثبت می‌شود.

هدف این سیستم:

* تشخیص خطاهای Runtime
* کمک به Debugging
* جمع‌آوری اطلاعات برای Bug Report
* کاهش زمان بررسی مشکلات

است.

---

# 📤 اشتراک‌گذاری گزارش خطا

VPX برای ارسال گزارش Diagnostics از یک `ContentProvider` اختصاصی استفاده می‌کند:

```text
com.vepro.code.vpxlog
```

این معماری امکان اشتراک‌گذاری کنترل‌شده فایل Log را بدون وابستگی به AndroidX FileProvider فراهم می‌کند.

> پیش از ارسال Log باید بررسی شود که اطلاعات حساس، کلید API یا داده خصوصی در گزارش وجود نداشته باشد.

---

# 🔐 Security & Privacy

امنیت یکی از بخش‌های اصلی معماری VPX است.

## API Keys

کلیدهای API با استفاده از قابلیت‌های امنیتی Android از جمله:

* Android Keystore
* رمزنگاری داده‌های حساس

محافظت می‌شوند.

## Encryption

برای داده‌های حساس ذخیره‌شده، از رمزنگاری **AES-256-GCM** استفاده می‌شود.

## Path Safety

دسترسی ابزارهای فایل‌سیستم به مسیرهای مجاز محدود می‌شود و Runtime برای جلوگیری از دسترسی خارج از Workspace یا محدوده مجاز، کنترل‌های مسیر دارد.

## Workspace / Private Data

معماری ابزارها میان Workspace مورد استفاده Agent و داده‌های خصوصی دستگاه تمایز قائل می‌شود.

## Network Security

VPX برای برخی Endpointهای حساس و آدرس‌های داخلی محدودیت‌هایی اعمال می‌کند.

همچنین برای کاهش ریسک سناریوهای SSRF، روی درخواست‌های خروجی کنترل‌های امنیتی اعمال می‌شود.

## API Key Redaction

داده‌های حساس مانند API Key نباید در Memory یا گزارش‌های غیرضروری باقی بمانند و سیستم‌های مرتبط با Memory و Diagnostics از مکانیزم‌های Redaction استفاده می‌کنند.

---

# ⚙️ Preflight Validation

پیش از اجرای درخواست‌های مدل، VPX می‌تواند تنظیمات اولیه را بررسی کند.

این مرحله برای شناسایی مشکلاتی مانند:

* Provider نامعتبر
* تنظیمات ناقص
* Endpoint نادرست
* Model نامشخص
* پیکربندی ناقص

پیش از اجرای کامل درخواست طراحی شده است.

این سیستم با لایه Error Handling در `LlmClient` نیز هماهنگ است.

---

# 🧯 Error Handling

VPX خطاهای Provider و Network را به‌صورت متمرکز مدیریت می‌کند.

خطاهای رایج شامل:

* Authentication
* Bad Request
* Forbidden
* Timeout
* TLS
* Network Failure
* Cancellation
* Provider Errors

از طریق لایه Client به خطاهای قابل‌فهم‌تر برای رابط کاربری تبدیل می‌شوند.

این معماری باعث می‌شود خطای خام Provider مستقیماً به کاربر نمایش داده نشود و امکان ارائه پیام مناسب برای هر سناریو وجود داشته باشد.

---

# ⚡ Background Execution

VPX از Android Foreground Service برای وظایف و سرویس‌هایی که نیاز به ادامه فعالیت در پس‌زمینه دارند استفاده می‌کند.

این معماری برای بخش‌هایی مانند:

* Taskهای طولانی
* Voice Trigger
* Floating Interaction
* عملیات Agent

کاربرد دارد.

> رفتار Background در Android به نسخه سیستم، سازنده دستگاه و Battery Optimization وابسته است.

---

# 🎨 User Interface

VPX از یک رابط کاربری سفارشی Android استفاده می‌کند.

اصول اصلی UI:

* طراحی مینیمال
* کنترل‌پذیری بالا
* عدم وابستگی غیرضروری به Frameworkهای سنگین
* پشتیبانی RTL/LTR
* طراحی اختصاصی Brand
* نمایش واضح وضعیت Agent و Toolها

VPX در بخش‌هایی از UI از:

* Vazirmatn
* JetBrains Mono
* آیکون‌ها و کامپوننت‌های سفارشی

استفاده می‌کند.

---

# 🌐 Persian / English

VPX از رابط دوزبانه پشتیبانی می‌کند.

### فارسی

* RTL
* چیدمان راست‌به‌چپ
* Mirror شدن Layout
* Typography مناسب فارسی

### English

* LTR
* Layout استاندارد انگلیسی

موارد زیر در هر دو زبان باید جهت مناسب خود را حفظ کنند:

* Code
* URL
* File Path
* API Key
* Command
* Technical Identifiers

---

# 🧱 Architecture

ساختار پروژه بر پایه چند لایه اصلی شکل گرفته است:

| Component               | مسئولیت                                |
| ----------------------- | -------------------------------------- |
| `AgentEngine`           | Orchestration و اجرای Agent            |
| `Tools`                 | ابزارهای قابل استفاده توسط مدل         |
| `LlmClient`             | ارتباط با Providerهای مدل              |
| `ChatStore`             | نگهداری وضعیت مکالمه                   |
| `Prefs`                 | تنظیمات و Preferenceها                 |
| `Preflight`             | بررسی اولیه Configuration              |
| `MarkdownRenderer`      | نمایش خروجی Markdown                   |
| `FileBrowser`           | مدیریت فایل و Workspace                |
| `SelfRepair`            | اجرای زیرسیستم Self-Repair             |
| `RepairSessionManager`  | مدیریت Repair Session                  |
| `VpxLogger`             | سیستم مرکزی Logging                    |
| `VpxLogProvider`        | اشتراک‌گذاری کنترل‌شده Log             |
| `VoiceTriggerService`   | سرویس تعاملات صوتی                     |
| `FloatingBubbleService` | مدیریت رابط شناور                      |
| `FloatingBubbleView`    | رابط گرافیکی Floating Bubble           |
| `MainActivity`          | رابط اصلی Agent                        |
| `SettingsActivity`      | تنظیمات و Diagnostics                  |
| `App`                   | Application lifecycle و Initialization |

---

# 🏗️ Technical Stack

| بخش          | فناوری                                |
| ------------ | ------------------------------------- |
| Platform     | Android                               |
| Minimum SDK  | API 23 / Android 6.0                  |
| Language     | Kotlin                                |
| UI           | Native Android / Custom UI            |
| Architecture | Agent + Tool Runtime                  |
| Model API    | Provider-specific + OpenAI-compatible |
| Local AI     | Ollama / LM Studio                    |
| Background   | Android Foreground Service            |
| Security     | Android Keystore / AES-256-GCM        |
| Logging      | Custom VPX Diagnostics                |
| License      | GNU AGPL v3.0                         |

VPX به‌صورت عمدی از وابستگی‌های غیرضروری جلوگیری می‌کند و هسته فعلی پروژه بدون وابستگی اجباری به AndroidX یا Jetpack Compose طراحی شده است.

---

# 🧪 Testing & Quality

VPX دارای تست‌های Regression برای بخش‌های هسته‌ای پروژه است.

هدف این تست‌ها جلوگیری از بازگشت خطاهای قبلی در بخش‌هایی مانند:

* Runtime
* Tool execution
* Core logic
* Configuration
* Error handling

است.

در کنار تست‌ها، سیستم Diagnostics و Logging برای بررسی مشکلات Runtime در نسخه‌های توسعه‌ای استفاده می‌شود.

---

# 📱 Requirements

حداقل:

```text
Android 6.0 (API 23)
```

برای استفاده از مدل‌های Cloud:

```text
Internet connection
API Key
```

برای استفاده از مدل‌های Local:

```text
Ollama or LM Studio
Local / LAN endpoint
```

قابلیت‌های مختلف ممکن است بر اساس نسخه Android، سازنده دستگاه، Provider و Model متفاوت باشند.

---

# 📥 Installation

نسخه‌های منتشرشده را از بخش Releases مخزن رسمی دریافت کنید:

**https://github.com/VPX-Vega-Pro-Extended/VPX/releases**

پس از نصب:

1. VPX را اجرا کنید.
2. وارد تنظیمات Provider شوید.
3. Provider موردنظر را انتخاب کنید.
4. `Base URL` را در صورت نیاز تنظیم کنید.
5. API Key را وارد کنید.
6. Model را انتخاب کنید.
7. Execution Mode مناسب را انتخاب کنید.
8. Agent را اجرا کنید.

> برای امنیت بیشتر، APK را فقط از منابع رسمی پروژه دریافت کنید.

---

# 🛠️ Build from Source

مخزن رسمی:

```bash
git clone https://github.com/VPX-Vega-Pro-Extended/VPX.git
cd VPX
```

ساخت نسخه Release:

```bash
gradle assembleRelease --no-daemon --console=plain
```

در صورت وجود اسکریپت‌های توسعه پروژه:

```bash
./mkapk.sh
```

اجرای تست‌ها:

```bash
./runtests.sh
```

در صورت نیاز:

```bash
chmod +x mkapk.sh runtests.sh
```

APK ساخته‌شده معمولاً در مسیر Build مربوط به Variant انتخاب‌شده قرار می‌گیرد.

---

# 🔧 Development

VPX برای توسعه قابلیت‌های جدید بر پایه چند اصل طراحی شده است:

### 1. User Control

Agent نباید بدون دلیل منطقی کنترل بیشتری از سطح انتخاب‌شده توسط کاربر دریافت کند.

### 2. Tool Isolation

عملیات واقعی از طریق ابزارهای مشخص و قابل‌کنترل انجام می‌شوند.

### 3. Workspace Safety

دسترسی فایل‌ها باید تا حد امکان به محدوده موردنیاز محدود شود.

### 4. Provider Independence

Runtime نباید به یک Provider خاص وابسته باشد.

### 5. Local-First

هر چیزی که بتواند روی دستگاه انجام شود، نباید الزاماً به یک Backend اختصاصی وابسته باشد.

### 6. Diagnostics

قابلیت‌های جدید باید تا حد امکان قابل Debug و قابل بررسی باشند.

### 7. Minimal Dependencies

از اضافه‌کردن Dependencyهای غیرضروری به پروژه جلوگیری می‌شود.

---

# 🗺️ وضعیت قابلیت‌ها

| قابلیت                     | وضعیت                 |
| -------------------------- | --------------------- |
| AI Agent Runtime           | ✅ فعال                |
| Multi-Step Tasks           | ✅ فعال                |
| Execution Modes            | ✅ فعال                |
| Filesystem Tools           | ✅ فعال                |
| Code/Text Editing          | ✅ فعال                |
| Web Search                 | ✅ فعال                |
| WebView Browsing           | ✅ فعال                |
| Multiple Providers         | ✅ فعال                |
| Ollama                     | ✅ پشتیبانی‌شده        |
| LM Studio                  | ✅ پشتیبانی‌شده        |
| Reasoning Control          | ✅ وابسته به مدل       |
| Preflight                  | ✅ فعال                |
| Secure Storage             | ✅ فعال                |
| Path Safety                | ✅ فعال                |
| Diagnostics Logging        | ✅ فعال                |
| Crash Logging              | ✅ فعال                |
| Log Sharing                | ✅ فعال                |
| Self-Repair Infrastructure | 🧪 در حال توسعه       |
| Voice Trigger              | 🧪 در حال توسعه/تکمیل |
| Floating Bubble            | 🧪 در حال توسعه       |
| Screen Control             | 🚧 در حال توسعه       |

---

# ⚠️ محدودیت‌ها و نکات مهم

* خروجی مدل‌های هوش مصنوعی ممکن است اشتباه یا ناقص باشد.
* پیش از اعمال تغییرات مهم، خروجی Agent را بررسی کنید.
* پیش از استفاده از Automatic Mode روی داده‌های مهم Backup تهیه کنید.
* API Key خود را در اختیار افراد یا سرویس‌های غیرضروری قرار ندهید.
* هزینه استفاده از APIهای Cloud بر اساس تعرفه Provider محاسبه می‌شود.
* قابلیت‌های مدل و Toolها به Provider و Model انتخاب‌شده وابسته هستند.
* دسترسی فایل‌ها به محدودیت‌های Android و مجوزهای کاربر وابسته است.
* عملکرد سرویس‌های Background ممکن است تحت تأثیر Battery Optimization سازنده دستگاه قرار گیرد.
* قابلیت‌های در حال توسعه ممکن است در نسخه‌های مختلف تغییر کنند.

---

# 🐛 Diagnostics & Bug Reports

برای بررسی مشکلات، از بخش:

```text
Settings → Logs & Diagnostics
```

استفاده کنید.

هنگام گزارش Bug، در صورت امکان اطلاعات زیر را ارائه دهید:

```text
VPX Version
Android Version
Device Model
Provider
Model
Execution Mode
Steps to Reproduce
Relevant Log
```

قبل از ارسال گزارش، اطلاعات حساس مانند:

* API Key
* Token
* Password
* اطلاعات خصوصی فایل‌ها
* داده‌های شخصی

را بررسی و حذف کنید.

---

# 🤝 Contributing

مشارکت در توسعه VPX آزاد است.

برای مشارکت:

1. مخزن را Fork کنید.
2. یک Branch مستقل ایجاد کنید.
3. تغییرات را اعمال کنید.
4. تست‌های مربوطه را اجرا کنید.
5. Commit واضح ایجاد کنید.
6. Pull Request ارسال کنید.

تغییرات بزرگ معماری یا تغییرات امنیتی بهتر است همراه با توضیح فنی کامل ارائه شوند.

---

# 🐞 Issues

گزارش Bug:

**https://github.com/VPX-Vega-Pro-Extended/VPX/issues**

Pull Request:

**https://github.com/VPX-Vega-Pro-Extended/VPX/pulls**

در گزارش Bug، اطلاعات حساس را ارسال نکنید.

---

# 📄 License

VPX تحت مجوز:

**GNU Affero General Public License v3.0**

منتشر می‌شود.

مشاهده متن کامل مجوز:

```text
LICENSE
```

یا:

**https://www.gnu.org/licenses/agpl-3.0.html**

---

# 🔗 Project

Official Repository:

**https://github.com/VPX-Vega-Pro-Extended/VPX**

Releases:

**https://github.com/VPX-Vega-Pro-Extended/VPX/releases**

Issues:

**https://github.com/VPX-Vega-Pro-Extended/VPX/issues**

Pull Requests:

**https://github.com/VPX-Vega-Pro-Extended/VPX/pulls**

---

<div align="center">

## ⚡ VPX — Vega Pro Extended

### Local-First · User-Controlled · Provider-Flexible · Extensible

**An AI Agent designed to give users more control over models, tools, files, workflows and execution.**

<br>

`Android` · `Kotlin` · `AI Agent` · `Local AI` · `Tool Runtime` · `Self-Repair`

<br>

**GNU AGPL-3.0**

</div>
