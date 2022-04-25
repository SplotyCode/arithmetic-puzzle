function renderPuzzle(data) {
    console.log(data)
    var number = $.templates("#template-number");
    var operator = $.templates("#template-operator");
    var result = $.templates("#template-result");
    const puzzle = $("#puzzle")
    puzzle.empty()
    puzzle.append(number.render({number: data.numbers[0]}))
    var expresion = data.numbers[0]
    for (var i = 0; i < data.operators.length; i++) {
        expresion += data.operators[i] + data.numbers[i + 1]
        puzzle.append(operator.render({operator: data.operators[i]}))
        puzzle.append(number.render({number: data.numbers[i + 1]}))
    }
    console.log(expresion)
    $(".operator").on('click', (a) => console.log(a))
    console.log($(".operator"))
    puzzle.append(result.render({result: data.result}))
}
function fetchPuzzle() {
    $.get("/api/puzzle?operands=" + $("#operators").val(), renderPuzzle)
}
$(function() {
    fetchPuzzle()
    $(document).on('change', 'select', function () {
        var correct = this.value === this.dataset.solution
        var element = $(this)
        element.toggleClass("text-danger", !correct)
        element.toggleClass("text-success", correct)
    });
    $("#fetch").click(fetchPuzzle)
})
