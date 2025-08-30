<!--
isExpanded = false;

function getIndex(el) {
  ind = null;
  for (i=0; i<document.layers.length; i++) {
    whichEl = document.layers[i];
    if (whichEl.id == el) {
      ind = i;
      break;
    }
  }
  return ind;
}

function arrange() {
  nextY = document.layers[firstInd].pageY + document.layers[firstInd].document.height;
  for (i=firstInd+1; i<document.layers.length; i++) {
    whichEl = document.layers[i];
    if (whichEl.visibility != "hide") {
      whichEl.pageY = nextY;
      nextY += whichEl.document.height;
    }
  }
}

function initIt(){
  if (NS4) {
    for (i=0; i<document.layers.length; i++) {
      whichEl = document.layers[i];
      if (whichEl.id.indexOf("Child") != -1) whichEl.visibility = "hide";
    }
    arrange();
  } else if(IE4) {
    tempColl = document.all.tags("DIV");
    for (i=0; i<tempColl.length; i++) {
      if (tempColl(i).className == "child") tempColl(i).style.display = "none";
    }
  } else if(DOM) {
    tempColl = document.getElementsByTagName("DIV");
    for (i=0; i<tempColl.length; i++) {
      if (tempColl(i).className == "child") tempColl(i).style.visibility = "hidden";
    }
  }
}

function expandIt(el) {
  if (!capable) return;
  if (IE4) {
    expandIE(el);
  } else if(NS4) {
    expandNS(el);
  } else if(DOM) {
    expandDOM(el);
  }
}

function expandIE(el) {
  whichEl = eval(el + "Child");
        whichIm = eval(el+"Img");

  if (whichEl.style.display == "none") {
    whichEl.style.display = "block";
    whichIm.src = "../images/minus.gif";
  }
  else {
    whichEl.style.display = "none";
    whichIm.src = "../images/plus.gif";
  }
    window.event.cancelBubble = true ;
}

function expandNS(el) {
  whichEl = eval("document." + el + "Child");
  whichIm = eval("document." + el + "Parent.document.images['imEx']");
  if (whichEl.visibility == "hide") {
    whichEl.visibility = "show";
    whichIm.src = "../images/minus.gif";
  }
  else {
    whichEl.visibility = "hide";
    whichIm.src = "../images/plus.gif";
  }
  arrange();
}

function expandDOM(el) {

  whichEl = document.getElementById(el + "Child");
    whichIm = document.getElementById(el + "Img");

  if (whichEl.style.visibility != "visible") {
    whichEl.style.visibility = "visible";
    whichIm.src = "../images/minus.gif";
  } else {
    whichEl.style.visibility = "hidden";
    whichIm.src = "../images/plus.gif";
  }

}

function showAll() {
  for (i=firstInd; i<document.layers.length; i++) {
    whichEl = document.layers[i];
    whichEl.visibility = "show";
  }
}

function expandAll(isBot) {
  newSrc = (isExpanded) ? "../images/plus.gif" : "../images/minus.gif";

  if (NS4) {
        // TR-02-01-99: Don't need that
        // document.images["imEx"].src = newSrc;
    for (i=firstInd; i<document.layers.length; i++) {
      whichEl = document.layers[i];
      if (whichEl.id.indexOf("Parent") != -1) {
        whichEl.document.images["imEx"].src = newSrc;
      }
      if (whichEl.id.indexOf("Child") != -1) {
        whichEl.visibility = (isExpanded) ? "hide" : "show";
      }
    }

    arrange();
   if (isBot && isExpanded) scrollTo(0,document.layers[firstInd].pageY);
  } else if(IE4) {
    divColl = document.all.tags("DIV");
    for (i=0; i<divColl.length; i++) {
      if (divColl(i).className == "child") {
        divColl(i).style.display = (isExpanded) ? "none" : "block";
      }
    }
    imColl = document.images.item("imEx");
    for (i=0; i<imColl.length; i++) {
      imColl(i).src = newSrc;
    }
  } else if(DOM) {
    divColl = document.getElementsByTagName("DIV");
    for (i=0; i<divColl.length; i++) {
      if (divColl(i).className == "child") {
        divColl(i).style.visibility = (isExpanded) ? "hidden" : "visible";
      }
    }
    imColl = document.getElementsByName("imEx");
    for (i=0; i<imColl.length; i++) {
      imColl(i).src = newSrc;
    }
  }

  isExpanded = !isExpanded;
}

with (document) {
  if(DOM) {
    var lstyle = "<style type='text/css'>";
    lstyle += ".child {font-family: Verdana, Arial, Helvetica, sans-serif; color: #FA8072; text-decoration:none; visibility:hidden}";
    lstyle += ".parent {font-family: Verdana, Arial, Helvetica, sans-serif; color: #000000; text-decoration:none;}";
    lstyle += ".item { color: yellow; text-decoration:none; font-size: 8pt;}";
    lstyle += ".highlight { color: red; font-size: 8pt;}";
    lstyle += ".heada { font: 12px/13px; Times}";
    lstyle += "DIV { color:black; }";
    lstyle += "</style>";
    write(lstyle);
  } else {
    write("<style type='text/css'>");
    if (NS4) {
            write(".parent {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt;link : yellow; text-decoration:none; position:absolute; visibility:hidden; color: black;}");
            write(".child {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt;link : yellow; position:absolute; visibility:hidden}");
            write(".item { color: yellow; link : yellow; text-decoration:none;}");
            write(".regular {font-family: Arial,Helvetica,sans-serif; position:absolute; visibility:hidden}");
            write("DIV { color:black; }");
    } else if(IE4) {
            write(".child {font-family: Verdana, Arial, Helvetica, sans-serif; color: #FA8072;font-size: 8pt; text-decoration:none; display:none}");
            write(".parent {font-family: Verdana, Arial, Helvetica, sans-serif; color: #000000;font-size: 10pt; text-decoration:none;}");
            write(".item { color: yellow; text-decoration:none;}");
            write(".highlight { color: red; font-size: 8pt;}");
            write(".heada { font: 12px/13px; Times}");
            write("DIV { color:black; }");
    }
    write("</style>");
  }
}

onload = initIt;

//-->
