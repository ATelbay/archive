import telebot
from telebot import types
import requests
import random
import goslate


@bot.message_handler(commands=['start'])
def send_welcome(message):
    bot.reply_to(message,
                 "Можете поздороваться со мной,"
                 " написав привет или попрощаться, написав пока. Для дополнительной"
                 " помощи напишите /help")


@bot.message_handler(commands=['help'])
def commands(message):
    bot.send_message(message.chat.id, "Список команд:\n" +
                     "/set_city - Установить свой город\n" +
                     "/weather - Узнать погоду\n" +
                     "/translate - Перевести с русского на английский\n" +
                     'Напишите привет, чтобы поздороваться\n' +
                     'Напишите пока, чтобы попрощаться\n' +
                     'Напишите "Как дела?", чтобы узнать у настроение бота\n' +
                     'Напишите "расскажи шутку", чтобы бот пошутил\n' +
                     'Напишите "расскажи факт", чтобы бот рассказал факт')


@bot.message_handler(commands=['translate'])
def send_translate(message):
    bot.send_message(message.chat.id, "Напишите слово для перевода")
    bot.register_next_step_handler(message, do_translate)


@bot.message_handler(commands=['weather'])
def send_weather(message):
    markup = types.ReplyKeyboardMarkup(row_width=3, one_time_keyboard=True, resize_keyboard=True)
    itembtn1 = types.KeyboardButton('Almaty')
    itembtn2 = types.KeyboardButton('New York')
    itembtn3 = types.KeyboardButton('London')
    markup.add(itembtn1, itembtn2, itembtn3)
    bot.send_message(message.chat.id, "Выберите город или напишите свой:", reply_markup=markup)
    bot.register_next_step_handler(message, send_start_weather)


@bot.message_handler(commands=['set_city'])
def set_city(message):
    markup = types.InlineKeyboardMarkup()
    markup.add(*[types.InlineKeyboardButton(text=name,
                                            callback_data=name) for name in ['Almaty',
                                                                             'New York',
                                                                             'Другой город']])
    bot.send_message(message.chat.id, "Укажите или напишите свой город:", reply_markup=markup)


@bot.callback_query_handler(func=lambda c: True)
def inline(c):
    s_city = c.data
    print(s_city)
    city_id = 0
    markup = types.InlineKeyboardMarkup()
    if s_city != 'Almaty':
        markup.add(*[types.InlineKeyboardButton(text=name,
                                                callback_data=name) for name in ['Almaty',
                                                                                 s_city,
                                                                                 'Другой город']])
    else:
        markup.add(*[types.InlineKeyboardButton(text=name,
                                                callback_data=name) for name in ['Almaty',
                                                                                 'New York',
                                                                                 'Другой город']])

    if c.data == 'Другой город':
        bot.edit_message_text(chat_id=c.message.chat.id,
                              message_id=c.message.message_id,
                              text='*Напишите свой город')
        bot.register_next_step_handler(c.message, change_city)

    else:
        try:
            res = requests.get("http://api.openweathermap.org/data/2.5/find",
                               params={'q': s_city, 'type': 'like', 'units': 'metric', 'lang': 'ru', 'APPID': appid})
            data = res.json()
            try:
                city_id = data['list'][0]['id']
            except Exception as e:
                print("Exception no city:", e)
                bot.send_message(c.message.chat.id, "Такого города я не нашел, но могу показать погоду в Астане")
                s_city = 'Astana'
                res = requests.get("http://api.openweathermap.org/data/2.5/find",
                                   params={'q': s_city, 'type': 'like', 'units': 'metric', 'lang': 'ru',
                                           'APPID': appid})
                data = res.json()
                city_id = data['list'][0]['id']

            res = requests.get("http://api.openweathermap.org/data/2.5/weather",
                               params={'id': city_id, 'units': 'metric', 'lang': 'ru', 'APPID': appid})
            data = res.json()
            print("conditions:", data['weather'][0]['description'])
            print("temp:", data['main']['temp'])
            print("temp_min:", data['main']['temp_min'])
            print("temp_max:", data['main']['temp_max'])
            print(str(c.message.chat.id) + '\n' +
                  str(c.message.message_id))
            temp = str(data['main']['temp'])
            weather = str(data['weather'][0]['description'])
            bot.edit_message_text(chat_id=c.message.chat.id,
                                  message_id=c.message.message_id,
                                  text='*Сегодня в ' + s_city + ' ' + weather + ', ' + temp + '°C', reply_markup=markup)
        except Exception as e:
            print("Exception (weather):", e)
            markup = types.InlineKeyboardMarkup()
            markup.add(*[types.InlineKeyboardButton(text=name,
                                                    callback_data=name) for name in ['Almaty',
                                                                                     'New York',
                                                                                     'Другой город']])
            bot.edit_message_text(chat_id=c.message.chat.id,
                                  message_id=c.message.chat_id,
                                  text='*Я не нашел такого города, извините...',
                                  reply_markup=markup)

        pass


def change_city(message):
    s_city = message.text
    markup = types.InlineKeyboardMarkup()
    markup.add(*[types.InlineKeyboardButton(text=name,
                                            callback_data=name) for name in ['Almaty',
                                                                             s_city,
                                                                             'Другой город']])
    city_id = 0
    try:
        res = requests.get("http://api.openweathermap.org/data/2.5/find",
                           params={'q': s_city, 'type': 'like', 'units': 'metric', 'lang': 'ru', 'APPID': appid})
        data = res.json()
        city_id = data['list'][0]['id']
        res = requests.get("http://api.openweathermap.org/data/2.5/weather",
                           params={'id': city_id, 'units': 'metric', 'lang': 'ru', 'APPID': appid})
        data = res.json()
        print("conditions:", data['weather'][0]['description'])
        print("temp:", data['main']['temp'])
        print("temp_min:", data['main']['temp_min'])
        print("temp_max:", data['main']['temp_max'])
        temp = str(data['main']['temp'])
        weather = str(data['weather'][0]['description'])
        bot.send_message(message.chat.id, "Сегодня в " + s_city + " " + weather + ", " + temp + "°C",
                         reply_markup=markup)
    except Exception as e:
        print("Exception (weather):", e)
        markup = types.InlineKeyboardMarkup()
        markup.add(*[types.InlineKeyboardButton(text=name,
                                                callback_data=name) for name in ['Almaty',
                                                                                 'New York',
                                                                                 'Другой город']])
        bot.send_message(message.chat.id, "Я не нашел такого города, извините...")
        bot.send_message(message.chat.id, "Укажите или снова поменяйте Ваш город:", reply_markup=markup)
        pass

    print(message.text)


def do_translate(message):
    gs = goslate.Goslate()
    try:
        print(gs.translate(message.text, 'de'))
        bot.send_message(message.chat.id, gs.translate(message.text, 'en'))
    except Exception as e:
        print("Exception (translate):", e)
        bot.send_message(message.chat.id, "Не могу перевести, извините...")
        pass


def send_start_weather(message):
    s_city = message.text
    markup = types.ReplyKeyboardRemove(selective=False)
    city_id = 0
    try:
        res = requests.get("http://api.openweathermap.org/data/2.5/find",
                           params={'q': s_city, 'type': 'like', 'units': 'metric', 'lang': 'ru', 'APPID': appid})
        data = res.json()
        city_id = data['list'][0]['id']
        res = requests.get("http://api.openweathermap.org/data/2.5/weather",
                           params={'id': city_id, 'units': 'metric', 'lang': 'ru', 'APPID': appid})
        data = res.json()
        print("conditions:", data['weather'][0]['description'])
        print("temp:", data['main']['temp'])
        print("temp_min:", data['main']['temp_min'])
        print("temp_max:", data['main']['temp_max'])
        temp = str(data['main']['temp'])
        weather = str(data['weather'][0]['description'])
        bot.send_message(message.chat.id, "Сегодня в " + s_city + " " + weather + ", " + temp + "°C",
                         reply_markup=markup)
    except Exception as e:
        print("Exception (weather):", e)
        bot.send_message(message.chat.id, "Я не нашел такого города, извините...")
        pass


@bot.message_handler(content_types=['text'])
def send_text(message):
    print(message.from_user.first_name)
    name = message.from_user.first_name
    if message.text.lower() == 'привет':
        bot.send_message(message.chat.id, 'Здравствуйте, ' + name + "!")
        bot.send_sticker(message.chat.id, 'CAADAgADjwMAAnwFBxumsVWtnos_NwI')
    elif message.text.lower() == 'пока':
        bot.send_message(message.chat.id, 'До новых встреч!')
        bot.send_sticker(message.chat.id, 'CAADAgADrQMAAnwFBxuAxpJr4vcp1QI')
    elif message.text.lower() == 'как дела?':
        markup = types.ReplyKeyboardMarkup(row_width=3, one_time_keyboard=True, resize_keyboard=True)
        markup.add(*[types.KeyboardButton(text=name) for name in ['Нормально', 'Плохо']])
        bot.send_message(message.chat.id, 'У меня все хорошо, а у Вас?', reply_markup=markup)
        bot.register_next_step_handler(message, send_answer)
    elif message.text.lower() == 'расскажи шутку':
        anekdot_list = ['— Вот интересно, в раю есть интернет? \n'
                        '— По-любому нет! \n'
                        '— А ты откуда знаешь? \n'
                        '— А потому что все провайдеры горят в аду!',
                        'Заходит бесконечное число математиков в бар,\n'
                        'а бармен говорит: "Ребята, ну пива-то на всех не хватит".',
                        'Давай пошлем сына за продуктами?\n'
                        '— Подожди, пусть придет, сядет за компьютер, а потом пошлем',
                        'Почему рыбы живут в соленой воде?\n'
                        'Потому что из-за перцовой воды они чихают',
                        'Объявление:  "В магазин матрёшек требуются старший менеджер,'
                        ' менеджер, младший менеджер, ассистент младшего менеджера"']
        bot.send_message(message.chat.id, anekdot_list[random.randint(0, len(anekdot_list) - 1)])
    elif message.text.lower() == 'расскажи факт':
        fact_list = ['Шах и мат с персидского означает "Король умер"',
                     'Самая крупная жемчужина в мире достигает 6 килограммов в весе.',
                     'В языке древних греков не существовало слова, которое обозначало религию.',
                     'Кофеин состоит из тех же веществ, что и кокаин, и героин.',
                     'В современной истории есть промежуток времени, когда на счетах компании “Apple”,'
                     ' было больше средств, чем у американского правительства.',
                     'Вероятность того, что вы погибните в авиакатастрофе,'
                     ' в несколько раз больше вероятности того, что вас съест пума.']
        bot.send_message(message.chat.id, fact_list[random.randint(0, len(fact_list) - 1)])


def send_answer(message):
    markup = types.ReplyKeyboardRemove(selective=False)
    if message.text.lower() == 'нормально':
        bot.send_message(message.chat.id, 'Прекрасно :)', reply_markup=markup)
    elif message.text.lower() == 'плохо':
        bot.send_message(message.chat.id, 'Жалко :(', reply_markup=markup)


@bot.message_handler(content_types=['sticker'])
def send_sticker(message):
    print(message)


bot.polling()
