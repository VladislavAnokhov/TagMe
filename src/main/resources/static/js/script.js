
document.addEventListener('DOMContentLoaded', function() {

    console.log('DOMContentLoaded');
   // const form = document.getElementById('filterForm');
    // Обработчик для кнопки "Поиск рандом"


    document.getElementById('randomSearch').addEventListener('click', function () {
        window.location.href = window.location.origin +'/find/random?reset=true'; // Перенаправление на URL случайного поиска
    });

    document.getElementById('tagSearch').addEventListener('click', function () {
        window.location.href =window.location.origin + '/find/byFilter';
    });

    const myPageButton = document.getElementById('myPage');
    if (myPageButton) {
        myPageButton.addEventListener('click', function () {
            const userId = document.getElementById('userId').value;
            window.location.href = window.location.origin + '/user/' + userId;
        });
    }


    document.getElementById('searchButton').addEventListener('click', function(e) {
        e.preventDefault();
        updateURLWithFilters();
    });

    document.querySelectorAll('.pagination a').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault(); // Отменяем стандартное поведение ссылки
            const pageNumber = this.getAttribute('data-page'); // Получаем номер страницы из атрибута
            submitFormWithPage(pageNumber);
            // Извлекаем текущие значения фильтров
            const gender = document.getElementById('genderSelect').value;
            const tag = document.getElementById('tagSelect').value;
            const limit = document.querySelector('[name="limit"]').value;

            // Формируем новый URL, сохраняя выбранные фильтры и номер страницы
            let newUrl = `/find/byFilter?page=${pageNumber}&limit=${limit}`;
            if (gender) newUrl += `&gender=${gender}`;
            if (tag) newUrl += `&tag=${tag}`;

            window.location.href = newUrl; // Перенаправляем на сформированный URL
        });
    });

});

// Функция для обновления URL с сохранением текущих фильтров и номера страницы
function updateURLWithFilters(pageNumber = document.getElementById('pageInput').value) {
    const gender = document.getElementById('genderSelect').value || null;
    const tag = document.getElementById('tagSelect').value || null;

    const form = document.getElementById('filterForm');
    let newUrl = `/find/byFilter?page=${pageNumber}&limit=5`; // Используем limit из формы или значение по умолчанию

    if (gender) newUrl += `&gender=${gender}`;
    if (tag) newUrl += `&tag=${tag}`;

    window.location.href = newUrl; // Перенаправляем на сформированный URL
}


function submitFormWithPage(pageNumber) {
    // Установка номера страницы перед отправкой формы
    document.getElementById('pageInput').value = pageNumber;
    document.getElementById('filterForm').submit();
}






function uploadPhoto() {
    const formData = new FormData();
    const fileInput = document.getElementById('photoUpload');
    const file = fileInput.files[0];
    formData.append("file", file);

    fetch(`/user/${userId}/upload/photo`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                // Если файл успешно загружен, перенаправляем пользователя на его страницу
                window.location.href = `/user/${userId}`;
            } else {
                // Обработка ошибки загрузки файла
                console.error('Ошибка загрузки файла');
            }
        })
        .catch(error => console.error('Ошибка загрузки файла:', error));
}

function deletePhoto(photoId) {
    fetch(`/user/${userId}/deletePhoto/${photoId}`, {
        method: 'POST',

    })
        .then(response => {
            if (response.ok) {
                alert('Фотография удалена');
                window.location.reload(); // Перезагрузить страницу, чтобы отразить изменения
            } else {
                alert('Ошибка при удалении фотографии');
            }
        })
        .catch(error => console.error('Ошибка:', error));
}

function saveTag() {
    const tagValue = document.getElementById('newTag').value;
    const formData = new FormData();
    formData.append('tag', tagValue); // Добавляем тег как параметр формы

    fetch(`/user/${userId}/saveTag`, {
        method: 'POST',
        body: formData // Отправляем данные как FormData
    }).then(response => {
        if (response.ok) {
            window.location.reload(); // Перезагрузка страницы для отображения изменений
        } else {
            alert('Ошибка при добавлении тега');
        }
    }).catch(error => console.error('Ошибка:', error));
}

function deleteTag(tagId) {
    fetch(`/user/${userId}/deleteTag/${tagId}`, {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            window.location.reload(); // Перезагрузить страницу, чтобы отразить изменения
        } else {
            alert('Ошибка при удалении тега');
        }
    }).catch(error => console.error('Ошибка:', error));
}

document.getElementById('addTagButton').addEventListener('click', function() {
    document.getElementById('addTagForm').style.display = 'block';
});