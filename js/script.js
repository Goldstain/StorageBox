
"use strict";


AOS.init();

//  работа слайдера из библиотеки swiper !!!!
const swiper = new Swiper('.swiper', {
    direction: 'horizontal', // направление слайдера (горизонтальное)
    loop: true, // зацикливание слайдера (последний слайд переходит в первый)
    slidesPerView: 1, // количество слайдов на экране
    spaceBetween: 30, // расстояние между слайдами
    centeredSlides: true, // центрирование слайдов
    // autoplay: {
    //       delay: 5000, // задержка между автоматическим листанием слайдов
    //     disableOnInteraction: false, // отключение автоматического листания после взаимодействия с пользователем
    // },
    pagination: {
        el: '.swiper-pagination', // элемент для отображения пагинации
        clickable: true, // возможность кликать на пагинацию для перехода к слайду
    },
    navigation: {
        nextEl: '.swiper-button-next', // элемент для кнопки "Вперед"
        prevEl: '.swiper-button-prev', // элемент для кнопки "Назад"
    },
});


