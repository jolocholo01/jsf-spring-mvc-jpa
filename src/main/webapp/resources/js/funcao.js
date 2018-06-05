
   function setTime() {
      $('#idPedidoTransferencia');
   }

setInterval("setTime()", 3000);
$('[data-toggle="tooltip"]').tooltip();
$(".tabela-horarios tbody td:empty").html(" ---");//text("---");//
var tooltip = $(document).ready(function () {
  $('.tooltip-right').tooltip({
    placement: 'right',
    viewport: {selector: 'body', padding: 2}
  })
  $('.tooltip-left').tooltip({
    placement: 'left',
    viewport: {selector: 'body', padding: 2}
  })
   $('.tooltip-top').tooltip({
    placement: 'top',
    viewport: {selector: 'body', padding: 2}
  })
  $('.tooltip-bottom').tooltip({
    placement: 'bottom',
    viewport: {selector: 'body', padding: 2}
  })
  $('.tooltip-viewport-right').tooltip({
    placement: 'right',
    viewport: {selector: '.container-viewport', padding: 2}
  })
  $('.tooltip-viewport-bottom').tooltip({
    placement: 'bottom',
    viewport: {selector: '.container-viewport', padding: 2}
  })
})




function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        
        reader.onload = function (e) {
            $('.profile-img-tag').attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    }
}
$(".profile-img").change(function(){
    readURL(this);
});


//=====================================================
$(document).ready(function() {
	
	
	$('.tblEditavel tbody tr').each(function() {
		$(this).children('td').each(function(p) {
			$(this).bind('click dblclick',function(i) {
				var valorInput = $(this).children();
				var conteudoOrginal = $(this).children().val();
					valorInput.bind('blur keydown',function(e) {
						var keyCode = e.which;
						var conteudoNovo = $(this).val();
						var posicao = p+1;
						if (keyCode == 13) {
							$(this).closest("tr").next().children('td:nth-child('+ posicao+ ')').trigger('dblclick');
						}
						
						});
					if(i.type=="dblclick"){
						$(this).children().select();
					}
						
					});
					
				});
			});
	})

// funcao que é chamado quando a informação do texto não está selecionado.
function limitarNumeroDigitado(obj, e) {
	var tecla = (window.event) ? e.keyCode : e.which;
	var texto = obj.value;
	var indexvir = texto.indexOf(",");
	var indexpon = texto.indexOf(".");
	
	if (tecla == 8 ){
		return true;
	}
	//teclado tab e zero
	if (tecla == 0){
		return true;
	}
	if (tecla !=13  && tecla != 44  && tecla != 46 && (tecla < 48 || tecla > 57) )
		return false;
	if (tecla == 44) {
		if (indexvir !== -1 || indexpon !== -1) {
			return false;
		}
		if (texto == 20)
			return false;
	}
	if (tecla == 46) {
		if (indexvir !== -1 || indexpon !== -1) {
			return false;
		}
		if (texto == 20)
			return false;
	}
	
	// verifica se o texto nota do aluno esta selelecionado
	if (obj.selectionStart == 0 && obj.selectionEnd == obj.value.length) {		
		if (String.fromCharCode(tecla) > 20
				|| String.fromCharCode(tecla) < 0)
			return false;

		}else {
		
	if ((texto + String.fromCharCode(tecla)) > 20
				|| (texto + String.fromCharCode(tecla)) < 0) {
			return false;
	}

}
}
// ===================================================
function formata_mascara(campo_passado, e) {
	var tecla = (window.event) ? e.keyCode : e.which;
	var mascara;
	var valor=campo_passado.value;
	switch (true) {
    case (valor.indexOf('+') >= 0):
    	mascara='#### ## ### ### ### ###';
        break;
    case (valor.indexOf('8') >= 0):
    	mascara='## ### ### ### ### ###';       
        break;  
        default:
        mascara='## ### ### ### ### ###';  
    }
	var campo = campo_passado.value.length;
	var saida = mascara.substring(0, 1);
	var texto = mascara.substring(campo);
	
	
	if (texto.substring(0, 1) != saida) {
		campo_passado.value += texto.substring(0, 1);
			}
}


// ===============================================
window.onload = function() {
	$('#idPanelGroup').ready(function() {
		$('#idPanelGroup').keyup();

	});
	$('#tblEditavel').ready(function() {
		var valorInput = $('.inputLancarNota').value;
		
		if(valorInput % 2==0){
			valorInput.replace(".0","");
		}

	});
	
}
function aceitarApenaNumero(obj, e) {
	var tecla = (window.event) ? e.keyCode : e.which;
	if (tecla == 8 || tecla == 0)
		return true;
	if (tecla < 48 || tecla > 57)
		return false;
}
function aceitarApenaNumeroComVirgula(obj, e) {
	var tecla = (window.event) ? e.keyCode : e.which;
	var texto = obj.value;
	var indexvir = texto.indexOf(",");
	var indexpon = texto.indexOf(".");
	
	if (tecla == 8 ){
		return true;
	}
	//teclado tab e zero
	if (tecla == 0){
		return true;
	}
	if (tecla !=13  && tecla != 44 && (tecla < 48 || tecla > 57) )
		return false;
	if (tecla == 44) {
		if (indexvir !== -1 || indexpon !== -1) {
			return false;
		}
		if (texto == 20)
			return false;
	}
	if (tecla == 46) {
		if (indexvir !== -1 || indexpon !== -1) {
			return false;
		}
		if (texto == 20)
			return false;
	}

}
function aceitarApenaNumeroComEspaco(obj, e) {
	var texto = obj.value;
	var tecla = (window.event) ? e.keyCode : e.which;
	alert(tecla);
}
//funcao que verifica a quntidade de carateraes de testo no input 
function quatidadeCarrateres(obj, quant, e) {
	var texto = obj.value;
	var tecla = (window.event) ? e.keyCode : e.which;;
	
	if (tecla == 8 ){
		return true;
	}
	//teclado tab e zero
	if (tecla == 0){
		return true;
	}
	
	if (obj.selectionStart == 0 && obj.selectionEnd == texto.length) {	
		return true;
	}
	if (texto.length >= quant){
		if (obj.selectionStart == 0 && obj.selectionEnd == (quant+1)) {	
			return true;
		}
		if (obj.selectionStart != 0 && obj.selectionEnd != (quant+1)) {	
			return false;
		}
}

}
