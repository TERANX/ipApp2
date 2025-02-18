// const TASK = {
// }



document.querySelector(`.content`).innerHTML = `<table class="text"></table>`
for (key in TASK) {
    let row = document.createElement(`tr`)
    row.innerHTML = `<td colspan="2">${key}</td>`
    document.querySelector(`.text`).appendChild(row)

}