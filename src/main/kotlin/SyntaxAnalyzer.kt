object SyntaxAnalyzer {

    // Основной метод анализа
    fun analyzeSyntax(tokens: List<String>, log: MutableList<String>): Boolean {
        var currentIndex = 0

        // Проверяем, что начинается с "begin"
        if (tokens.getOrNull(currentIndex) != "begin") {
            log.add("Синтаксическая ошибка: программа должна начинаться с 'begin'.")
            return false
        }
        currentIndex++

        // Основной цикл анализа структуры программы
        while (currentIndex < tokens.size) {
            when (tokens[currentIndex]) {
                "dim" -> currentIndex = parseDescription(tokens, currentIndex, log)
                "if", "for", "do", "input", "output", "let", "{" -> currentIndex = parseOperator(tokens, currentIndex, log)
                "end" -> {
                    if (currentIndex != tokens.size - 1) {
                        log.add("Синтаксическая ошибка: код после 'end'.")
                        return false
                    }
                    return true
                }
                else -> {
                    log.add("Синтаксическая ошибка: неизвестный элемент '${tokens[currentIndex]}'.")
                    return false
                }
            }

            if (currentIndex == -1) return false // Если в процессе анализа нашли ошибку
        }

        log.add("Синтаксическая ошибка: программа должна заканчиваться 'end'.")
        return false
    }

    // Парсер для <описание>
    private fun parseDescription(tokens: List<String>, currentIndex: Int, log: MutableList<String>): Int {
        var index = currentIndex + 1

        // Проверяем идентификатор
        if (tokens.getOrNull(index)?.matches(Regex("[a-zA-Z_][a-zA-Z0-9_]*")) == true) {
            index++
        } else {
            log.add("Синтаксическая ошибка: ожидается идентификатор после 'dim'.")
            return -1
        }

        // Проверяем дополнительные идентификаторы
        while (tokens.getOrNull(index) == ",") {
            index++
            if (tokens.getOrNull(index)?.matches(Regex("[a-zA-Z_][a-zA-Z0-9_]*")) == true) {
                index++
            } else {
                log.add("Синтаксическая ошибка: ожидается идентификатор после ','.")
                return -1
            }
        }

        // Проверяем <тип>
        if (tokens.getOrNull(index) in listOf("%", "!", "$")) {
            index++
        } else {
            log.add("Синтаксическая ошибка: ожидается тип (% | ! | $).")
            return -1
        }

        return index
    }

    // Парсер для <оператор>
    private fun parseOperator(tokens: List<String>, currentIndex: Int, log: MutableList<String>): Int {
        var index = currentIndex

        when (tokens[index]) {
            "if" -> { /* Обработка условного оператора */ }
            "for" -> { /* Обработка цикла */ }
            "do" -> { /* Обработка условного цикла */ }
            "input" -> { /* Обработка ввода */ }
            "output" -> { /* Обработка вывода */ }
            "let" -> { /* Обработка присваивания */ }
            "{" -> { /* Обработка составного оператора */ }
            else -> {
                log.add("Синтаксическая ошибка: неизвестный оператор '${tokens[index]}'.")
                return -1
            }
        }

        return index + 1
    }
}
