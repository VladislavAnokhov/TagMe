/*
package com.javarush.TagMe.repositories;

import com.javarush.TagMe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface deposit {


    @Query(value = "SELECT * FROM users ORDER BY RANDOM()", nativeQuery = true)
List<User> findAllRandom();
    @Query("SELECT u FROM User u WHERE u.gender = :gender")
    Page<User> findByGender(@Param("gender") String gender, Pageable pageable);
    @Query(value ="SELECT u FROM users u JOIN u.tagsList t WHERE t.tag = :tag",nativeQuery = true)
    Page<User> findByTag(@Param("tag") String tag, Pageable pageable);

    @Query("SELECT DISTINCT t.tag FROM Tag t")
    List<String> findDistinctTags();
    @Query("SELECT u FROM User u JOIN u.tagsList t WHERE u.gender = :gender AND t.tag = :tag")
    Page<User> findByGenderAndTag(@Param("gender") String gender, @Param("tag") String tag, Pageable pageable);
    User findByEmail(String email);



document.querySelectorAll('.pagination a').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault(); // Отменяем стандартное поведение ссылки
            const pageNumber = this.getAttribute('data-page'); // Получаем номер страницы из атрибута
            const isFilterPage = window.location.pathname.includes('/find/byFilter'); // Проверяем, является ли текущая страница страницей поиска по фильтрам

            const baseUrl = isFilterPage ? '/find/byFilter' : '/find/random'; // Определяем базовый URL в зависимости от страницы
            // Перенаправляем на страницу с учетом выбранной страницы и лимита
            window.location.href = `${window.location.origin}${baseUrl}?page=${pageNumber}&limit=5`;
        });
    });
}
function updateURLWithFilters(pageNumber =1) {
        const form = document.getElementById('filterForm');
        const gender = document.getElementById('genderSelect').value || null;
        const tag = document.getElementById('tagSelect').value || null;
        const pageInput = document.getElementById('pageInput');
    let pageValue = pageInput.value;


    // Установка значения по умолчанию для pageNumber, если оно не предоставлено
    if (!pageValue) {
        pageValue = '0'; // Значение по умолчанию для pageNumber
    }

        const limit = document.querySelector('[name="limit"]').value || '5'; // Установка значения по умолчанию для limit

        const queryParams = new URLSearchParams(window.location.search);
    // Переустановка значений gender и tag в queryParams
    if (gender) queryParams.set('gender', gender); else queryParams.delete('gender');
    if (tag) queryParams.set('tag', tag); else queryParams.delete('tag');

    // Обновление page и limit в queryParams
    queryParams.set('page', pageValue);
    queryParams.set('limit', limit);

    // Формирование итогового URL и перенаправление
    window.location.href = `${form.action}?${queryParams.toString()}`;
}
*/
