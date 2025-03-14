"use strict";


document.addEventListener('DOMContentLoaded', function() {
    // Получаем все ячейки в теле таблицы
    const tableCells = document.querySelectorAll('.tbody td');

    // Обработчик для наведения на ячейку
    tableCells.forEach(cell => {
        cell.addEventListener('mouseenter', function() {
            // Находим индекс столбца
            const columnIndex = Array.from(cell.parentNode.children).indexOf(cell);

            // Подсвечиваем все ячейки в этом столбце
            document.querySelectorAll('.tbody tr').forEach(row => {
                row.children[columnIndex].classList.add('highlight');
            });

            // Подсвечиваем соответствующий заголовок
            document.querySelectorAll('.sticky-header th')[columnIndex].classList.add('highlight');
        });

        // Обработчик для выхода мышки
        cell.addEventListener('mouseleave', function() {
            // Убираем подсветку со всех ячеек в этом столбце
            const columnIndex = Array.from(cell.parentNode.children).indexOf(cell);
            document.querySelectorAll('.tbody tr').forEach(row => {
                row.children[columnIndex].classList.remove('highlight');
            });

            // Убираем подсветку с заголовка столбца
            document.querySelectorAll('.sticky-header th')[columnIndex].classList.remove('highlight');
        });
    });




    const productLinks = document.querySelectorAll('.article-name');
    const popup = document.createElement('div');
    popup.classList.add('product-popup');
    document.body.appendChild(popup);

    productLinks.forEach(link => {
        link.addEventListener('mouseenter', function (e) {
            const photoUrl = link.getAttribute('data-photo-url');
            if (photoUrl) {
                popup.innerHTML = `<img src="${photoUrl}" alt="Фото товару">`;
                const rect = link.getBoundingClientRect();
                popup.style.left = `${rect.left}px`;
                popup.style.top = `${rect.bottom + window.scrollY}px`;
                popup.style.display = 'block';
            }
        });

        link.addEventListener('mouseleave', function () {
            popup.style.display = 'none';
        });
    });
});