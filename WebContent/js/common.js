function CreateRequest()
{
    var request = false;

    if (window.XMLHttpRequest)
    {
        //Gecko-совместимые браузеры, Safari, Konqueror
        request = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        //Internet explorer
        try
        {
             request = new ActiveXObject("Microsoft.XMLHTTP");
        }    
        catch (CatchException)
        {
             request = new ActiveXObject("Msxml2.XMLHTTP");
        }
    }
 
    if (!request)
    {
        alert("Can't create XMLHttpRequest");
    }
    
    return request;
} 

function getData(listURL) {
	let request = CreateRequest();
   	request.open("GET", listURL, true); 
   	request.onreadystatechange = function () {
	if (request.readyState == 4) {
            if (request.status == 200){
            	showTable(request);
			}
           else
            {
            	document.write("Server's response " + request.status);      
            }
        }
    };
    request.send(null);
 }

function getAnotherData(url) {
	var request = CreateRequest();
   	request.open("GET", url, true); 
   	request.onreadystatechange = function () {
	if (request.readyState == 4) {
            if (request.status == 200){
            	switch(url) {
            	  case 'country':
            		  countries = JSON.parse(request.responseText);
            		  //alert(countries[1].countryName);
            	    break;
            	  default:
            	    alert("Can't get data from AnotherData");
            	} 
			}
        }
	};
    request.send(null);
 }

function deleteData(jsonId, deleteURL){
	var data = parsedJSON[jsonId];
	sendData('POST', deleteURL, data);
}

function saveData(addURL){
	var object = create();
	sendData('POST', addURL, object);
	hideModal();
	//alert(JSON.stringify(object));
	/*let scrollHeight = Math.max(
			  document.body.scrollHeight, document.documentElement.scrollHeight,
			  document.body.offsetHeight, document.documentElement.offsetHeight,
			  document.body.clientHeight, document.documentElement.clientHeight
			);*/
	window.scrollTo(0, document.body.scrollHeight);
	//window.scrollTo(0, document.body.scrollHeight || document.documentElement.scrollHeight);
}

function updateData(editURL){
	var object = create();
	object.id = document.getElementById("hiddenId").value;
	sendData('POST', editURL, object);
	hideModal();
}

function sendData(method, url, object){
	var request = CreateRequest();
	request.open(method, url, true);
	request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');

	request.onreadystatechange = function() {
		if (this.readyState === 4 && this.status === 200) {
			console.log('Succeed');
			getData(listURL);
		} else {
			console.log('Server error');
		}
	};

	request.onerror = function() {
		console.log('Something went wrong');
	};

	request.send(JSON.stringify(object));
}

function clearInput() {
    var inputs = [] ;
    inputs = document.getElementsByClassName("cleaningform");
    for(var i=0; i<inputs.length ; i++){
    	inputs[i].value = "";
    }
} 

function cancel(){
	hideModal();
}

function chooseAction(id){
	var button = document.getElementById('doSmth');
	if (id == "bAdd") {
		button.setAttribute('value', 'Save');
		button.setAttribute('onclick', "saveData('"+ addURL + "')");
	} else {
		button.setAttribute('value', 'Update');
		button.setAttribute('onclick', "updateData('"+ editURL + "')");	
	//	button.setAttribute('onclick', "updateData()");		
	}
	openWin();
}

var modal = document.querySelector('.modal');
var overflow = document.createElement('div');

function openWin() {
	overflow.className = "overflow";
	document.body.appendChild(overflow);
	var height = modal.offsetHeight;
	modal.style.marginTop = - height/2 + "px";
	modal.style.top = "50%";
//	modal.style.left = "37%";
	modal.style.position = "fixed";
	modal.style.transform = "translate(-50%, 0%)";
}

//overflow.onclick = hideModal();

function hideModal() {
    modal.style.top = "-100%";
    clearInput();
    overflow.remove();   
}