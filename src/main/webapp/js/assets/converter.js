;(function () {
  function Converter() {
    this.htmlEditor = document.querySelector('#html')
	
    this.markDownPreviewer = document.querySelector('.markdown-previewer')
  
    this.bindEvents()
  }

  Converter.prototype.bindEvents = function () {
    var self  = this
    // var html = this.htmlEditor.value
	var html = this.htmlEditor.innerText
    function onChange() {
		// var newHTML = self.htmlEditor.value
		var newHTML = self.htmlEditor.innerText
		try {
		  self.setM(h2m(newHTML, {converter: 'CommonMark'}))
		} catch(e) {
		}
		html = newHTML
	}
	onChange()
    // this.htmlEditor.addEventListener('keyup', onChange)
    // this.htmlEditor.addEventListener('paste', onChange)
    
  }

  Converter.prototype.setM = function (md) {
    console.log(JSON.stringify(md))
    this.markDownPreviewer.innerHTML = markdown.toHTML(md)
  }
  
  new Converter()
})()
