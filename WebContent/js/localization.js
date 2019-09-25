var currentLocale = readCookie("locale");
var selectedLanguage = document.getElementById("locale");

function reload()
{
	setCookieLocale();
	window.location.reload();
}

function setCookieLocale()
{    
    if (selectedLanguage.selectedIndex > 0) 
    {
        currentLocale = selectedLanguage.options[selectedLanguage.selectedIndex].value; 
    } else {
    	currentLocale = "en";
    }
    setCookie("locale", currentLocale);
    setPageText();
}

function setSelectValue()
{
    for (let i = 0; i < selectedLanguage.options.length; i++) 
    {
        if (selectedLanguage.options[i].value == currentLocale) 
        {
            selectedLanguage[i].selected = true;
        } 
    }
}

function setLocalization(){
    setSelectValue();
    setPageText();
}

function setPageText()
{
	for(let key in idKeyMapping)
    {
		if (document.body.contains(document.getElementById(key)))
        {
        	setText(key, getTextByKey(idKeyMapping[key]));
        }
  }
}

function setElementText(key)
{
    return getTextByKey(idKeyMapping[key]);
}

function setText(elementId, text)
{
    document.getElementById(elementId).innerHTML = text;
}

function getTextByKey(key)
{
    return localizationText[currentLocale][key]; 
}


idKeyMapping = {
		"account-caption" : "account-caption.text",
        "login-email-caption": "login-email-caption.text",
        "password-caption": "password-caption.text",
        "role-admin-caption": "role-admin-caption.text",
        "role-client-caption": "role-client-caption.text",
        "login-button": "login-button.text",
        
        "login-logout-button": "login-button.text",
        "login": "login-button.text",
        "logout": "logout.text",

        "cancel-button": "cancel-button.text",
        "header-caption": "header-caption.text",
        "cabinet-button": "cabinet-button.text",
        "pick-up-location-caption": "pick-up-location-caption.text",
        "pick-up-date-caption": "pick-up-date-caption.text",
        "drop-off-location-caption": "drop-off-location-caption.text",
        "drop-off-date-caption": "drop-off-date-caption.text",
        "search-button": "search-button.text",
        "footer-caption": "footer-caption.text",
        "available-car-caption": "available-car-caption.text",
        
        "category": "category.text",
        "year": "year.text",
        "color": "color.text",
        "transmission": "transmission.text",
        "number-of-seats": "number-of-seats.text",
        "number-of-suitcases": "number-of-suitcases.text",
        "air-conditioning": "air-conditioning.text",
        "rate-number": "rate-number.text",
        "rental-price": "rental-price.text",
        "total-rental": "total-rental.text",
        "select": "select.text",
     
        "reservation-button": "reservation-button.text",
        "car-new-damage-caption": "car-new-damage-caption.text",
        "car-caption": "car-caption.text",
        "car-number-caption": "car-number-caption.text",
        "client-caption": "client-caption.text",
        "type-damage-caption": "type-damage-caption.text",
        "info-caption": "info-caption.text",
        "amount-caption": "amount-caption.text",
        "commit-button": "commit-button.text",
        "clients-birth-year": "clients-birth-year.text", 
        "note": "note.text", 
        "status": "status.text",

        "search-car-button": "search-car-button.text",
        "pay-button": "pay-button.text",

        "reservation-info-caption": "reservation-info-caption.text",
        "period-caption": "period-caption.text",
        "back-to-page-button": "back-to-page-button.text",
        "pick-up-caption": "pick-up-caption.text",
        "drop-off-caption": "drop-off-caption.text",

        "credit-card-type-caption": "credit-card-type-caption.text",
        "credit-card-number-caption": "credit-card-number-caption.text",
        "confirm-button": "confirm-button.text",
        "payment-caption": "payment-caption.text",

        "traveller-details-caption": "traveller-details-caption.text",
        "first-name-caption": "first-name-caption.text",
        "last-name-caption": "last-name-caption.text",
        "passport-number-caption": "passport-number-caption.text",
        "driver-license-caption": "driver-license-caption.text",
        "birth-date-caption": "birth-date-caption",
        "phone-number-caption": "phone-number-caption",
        "email-caption": "email-caption",
        "confirm-next-button": "confirm-next-button.text"
};

localizationText = {
    "ru" : {
        "account-caption.text": "АККАУНТ",
        "login-email-caption.text": "Почта: ",
        "password-caption.text": "Пароль: ",
        "role-admin-caption.text": "Администратор",
        "role-client-caption.text": "Клиент",
        "login-button.text": "Войти",
        "cancel-button.text": "Отмена",

//        "login.text": "Войти",
        "logout.text": "Выйти",
        
        "header-caption.text": "Прокат автомобилей",
        "cabinet-button.text": "Мои заказы",
        "pick-up-location-caption.text": "Место получения:",
        "pick-up-date-caption.text": "Дата получения:",
        "drop-off-location-caption.text": "Место возврата:",
        "drop-off-date-caption.text": "Дата возврата:",
        "search-button.text": "Найти машину",
        "footer-caption.text": "Хорошей поездки :)",
        
        "available-car-caption.text": "Доступные машины",
        "category.text": "Категория",
        "year.text": "Год",
        "color.text": "Цвет",
        "transmission.text": "Тип трансмиссии",
        "number-of-seats.text": "Количество мест",
        "number-of-suitcases.text": "Место для багажа",
        "air-conditioning.text": "Кондиционер",
        "rate-number.text": "Рейтинг",
        "rental-price.text": "Цена ренты",
        "total-rental.text": "Полная стоимость",
        "select.text": "Выбрать",

        "reservation-button.text": "Показать заказы",
        "car-new-damage-caption.text": "Добавить новое повреждение",
        "car-caption.text": "Автомобиль:",
        "car-number-caption.text": "Номер автомобиля:",
        "client-caption.text": "Клиент:",
        "type-damage-caption.text": "Тип повреждения:",
        "info-caption.text": "Информация:",
        "amount-caption.text": "Стоимость, $:",
        "commit-button.text": "Принять",
        "clients-birth-year.text": "Год рождения клиента", 
        "note.text": "Пометка", 
        "status.text": "Статус",

        "search-car-button.text": "Поиск автомобиля",
        "pay-button.text": "Оплатить",

        "reservation-info-caption.text": "ИНФОРМАЦИЯ О ЗАКАЗЕ",
        "period-caption.text": "ДНЕЙ АРЕНДЫ: ",
        "back-to-page-button.text": "OK",
        "pick-up-caption.text": "ВЗЯТЬ",
        "drop-off-caption.text": "ВЕРНУТЬ",

        "credit-card-type-caption.text": "Тип кредитной карты",
        "credit-card-number-caption.text": "Номер кредитной карты",
        "confirm-button.text": "Подтвердить оплату",
        "payment-caption.text": "Ваша оплата составляет: ",
        
        "traveller-details-caption.text": "Форма клиента",
        "first-name-caption.text": "Имя:",
        "last-name-caption.text": "Фамилия:",
        "passport-number-caption.text": "Номер паспорта:",
        "driver-license-caption.text": "Номер прав вождения:",
        "birth-date-caption": "Дата рождения:",
        "phone-number-caption": "Номер телефона:",
        "email-caption": "Электронная почта:",
        "confirm-next-button.text": "Далее"
    },
    "en" : {
        
        "account-caption.text": "ACCOUNT LOGIN",
        "login-email-caption.text": "E-mail: ",
        "password-caption.text": "Password:",
        "role-admin-caption.text": "Admin",
        "role-client-caption.text": "Client",
        "login-button.text": "Log in",
        "cancel-button.text": "Cancel",

     //   "login": "login.text",
        "logout.text": "Log Out",
      
        "header-caption.text": "Car Rental System",
        "cabinet-button.text": "My Reservations",
        "pick-up-location-caption.text": "Pick Up Location:",
        "pick-up-date-caption.text": "Pick Up Date:",
        "drop-off-location-caption.text": "Drop Off Location:",
        "drop-off-date-caption.text": "Drop Off Date:",
        "search-button.text": "Find Your Car",
        "footer-caption.text": "Have a nice trip :)",
     
        "available-car-caption.text": "Available cars:",
        "category.text": "Category",
        "year.text": "Year",
        "color.text": "Color",
        "transmission.text": "Transmission",
        "number-of-seats.text": "Number of Seats",
        "number-of-suitcases.text": "Number of Suitcases",
        "air-conditioning.text": "Air conditioning",
        "rate-number.text": "Rate Number",
        "rental-price.text": "Rental Price",
        "total-rental.text": "Total Rental",
        "select.text": "Select",

        "reservation-button.text": "Show Reservations",
        "car-new-damage-caption.text": "Add new car's damage",
        "car-caption.text": "Car:",
        "car-number-caption.text": "Car's Number:",
        "client-caption.text": "Client:",
        "type-damage-caption.text": "Type of Damage:",
        "info-caption.text": "Information:",
        "amount-caption.text": "Amount, $:",
        "commit-button.text": "Commit",   
        "clients-birth-year.text": "Client's Birth Year", 
        "note.text": "Note", 
        "status.text": "Status",

        "search-car-button.text": "Search for a car",
        "pay-button.text": "Pay",

        "reservation-info-caption.text": "RESERVATION INFORMATION",
        "period-caption.text": "DAY RENTAL: ",
        "back-to-page-button.text": "OK",
        "pick-up-caption.text": "PICK UP",
        "drop-off-caption.text": "DROP OFF",

        "credit-card-type-caption.text": "Credit Card Type",
        "credit-card-number-caption.text": "Credit Card Number",
        "confirm-button.text": "Confirm Payment",
        "payment-caption.text": "Your payment is: ",

        "traveller-details-caption.text": "Client Form",
        "first-name-caption.text": "First Name:",
        "last-name-caption.text": "Last Name:",
        "passport-number-caption.text": "Passport Number:",
        "driver-license-caption.text": "Driver License Number:",
        "birth-date-caption": "Birth Date:",
        "phone-number-caption": "Phone Number:",
        "email-caption": "Email Address:",
        "confirm-next-button.text": "Next"
    }
};
