# JGuiWrapper
> Библиотека для создания пользовательских графических интерфейсов на серверах Minecraft с использованием ядра PaperMc 1.20-1.21.6

### Начало работы
Для работы библиотеки, требуется произвести инициализацию от имени любого плагина (чтобы зарегистрировать нужные слушатели ивентов инвентаря)

Главный класс плагина
```java
@Override
public void onEnable() {
    JGuiInitializer.init(this);
}
```

### Графические интерфейсы

#### AbstractGui
Самый простой из всех возможных графических интерфейсов библиотеки, позволяет слушать все ивенты инвентаря, а также изменять его внешний вид динамически (тип, размер, заголовок) с помощью NMS

#### SimpleGui
Добавляет простой функционал реагирования на клики по конкретным слотам с помощью примитивных обработчиков

#### AdvancedGui
Более продвинутый интерфейс, добавляет специальные контроллеры предметов для динамического обновления содержимого инвентаря с использованием внешней оболочки ItemStack - ItemWrapper

