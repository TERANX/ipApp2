

$(document).ready(function() {
    let optionCount = 0;

    // Функция для динамического добавления новых вариантов ответа
    function addOption() {
        let optionDiv = $('<div class="option form-group"></div>');
        let optionInput = $('<input type="text" class="form-control" th:name="options[__${optionCount}__]" required>');
        let correctCheckbox = $('<input type="checkbox" class="form-check-input" th:name="correctOptions[__${optionCount}__]">');

        optionDiv.append(optionInput);
        optionDiv.append($('<div class="form-check mt-2"></div>').append(correctCheckbox));

        $('#options').append(optionDiv);
        optionCount++;
    }

    // Обработчик события клика на кнопку "Добавить ответ"
    $('#addOption').click(function() {
        addOption();
    });
});