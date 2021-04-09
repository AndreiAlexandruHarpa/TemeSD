import os
import sys

from PyQt5.QtCore import pyqtSlot
from PyQt5.QtWidgets import *
from PyQt5 import QtCore
from PyQt5.uic import loadUi
from mq_communication import RabbitMq


def debug_trace(ui=None):
    from pdb import set_trace
    QtCore.pyqtRemoveInputHook()
    set_trace()
    # QtCore.pyqtRestoreInputHook()


class LibraryApp(QWidget):
    ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

    def __init__(self):
        super(LibraryApp, self).__init__()
        ui_path = os.path.join(self.ROOT_DIR, 'exemplul_2.ui')
        loadUi(ui_path, self)
        self.search_btn.clicked.connect(self.search)
        self.save_as_file_btn.clicked.connect(self.save_as_file)
        self.rabbit_mq = RabbitMq(self)
        self.new_btn.clicked.connect(self.on_click)
        self.form = None

    @pyqtSlot()
    def on_click(self):
        self.form = Form(self)
        self.form.show()

    def set_response(self, response):
        self.result.setText(response)

    def send_request(self, request):
        self.rabbit_mq.send_message(message=request)
        self.rabbit_mq.receive_message()

    def search(self):
        search_string = self.search_bar.text()
        request = None
        if not search_string:
            if self.json_rb.isChecked():
                request = 'print:json'
            elif self.html_rb.isChecked():
                request = 'print:html'
            elif self.xml_rb.isChecked():
                print("merge")
                request = 'print:xml'
            else:
                request = 'print:raw'
        else:
            if self.author_rb.isChecked():
                request = 'find:author={}'.format(search_string)
            elif self.title_rb.isChecked():
                request = 'find:title={}'.format(search_string)
            else:
                request = 'fin"html" -> libraryPrinter.printHTML(libraryDAO.getBooks())d:publisher={}'.format(search_string)

            if self.json_rb.isChecked():
                request += ',print:json'
            elif self.html_rb.isChecked():
                request += ',print:html'
            elif self.xml_rb.isChecked():
                print("merge")
                request += ',print:xml'
            else:
                request += ',print:raw'
        self.send_request(request)

    def save_as_file(self):
        options = QFileDialog.Options()
        options |= QFileDialog.DontUseNativeDialog
        file_type = ""
        if self.json_rb.isChecked():
            file_type += 'JSON (*.json)'
        elif self.html_rb.isChecked():
            file_type += 'HTML (*.html)'
        elif self.xml_rb.isChecked():
            file_type += 'XML (*.xml)'
        else:
            file_type += 'Text (*.txt)'

        file_path = str(
            QFileDialog.getSaveFileName(self,
                                        'Salvare fisier',
                                        options=options, filter=file_type))
        if file_path:
            file_path = file_path.split("'")[1]
            file_path += file_type.split("*")[1][0:-1]
            try:
                with open(file_path, 'w') as fp:
                    if file_path.endswith(".html"):
                        fp.write(self.result.toHtml())
                    else:
                        fp.write(self.result.toPlainText())
            except Exception as e:
                print(e)
                QMessageBox.warning(self, 'Exemplul 2',
                                    'Nu s-a putut salva fisierul')

    def add_book(self, autor, titlu, editura, content):
        request = ""

        if len(autor):
            request += "autor:{}".format(autor)
        else:
            request += "autor:null"

        if len(titlu):
            request += ",titlu:{}".format(titlu)
        else:
            request += ",titlu:null"

        if len(editura):
                request += ",editura:{}".format(editura)
        else:
            request += ",editura:null"
        if len(content):
            request += ",continut:{}".format(content)
        else:
            request += ",titlu:null"

        self.send_request(request)


class Form(QMainWindow):
    def __init__(self, library):
        super().__init__()
        self.library = library
        self.button = QPushButton('Adauga', self)
        self.textboxEditura = QLineEdit(self)
        self.textboxTitlu = QLineEdit(self)
        self.textboxAutor = QLineEdit(self)
        self.textboxContinut = QLineEdit(self)
        self.labelEditura = QLabel('Editura', self)
        self.labelTitlu = QLabel('Titlu', self)
        self.labelAutor = QLabel('Autor', self)
        self.labelContinut = QLabel('Continut', self)
        self.setGeometry(0, 0, 400, 400)
        self.title = "Form"
        self.left = 10
        self.top = 10
        self.width = 380
        self.height = 400

        self.initUI()

    def initUI(self):
        self.setWindowTitle(self.title)
        self.setGeometry(self.left, self.top, self.width, self.height)

        self.labelAutor.move(20, 30)
        self.labelTitlu.move(20, 90)
        self.labelEditura.move(20, 150)
        self.labelContinut.move(20, 210)

        self.textboxAutor.move(70, 20)
        self.textboxAutor.resize(280, 40)

        self.textboxTitlu.move(70, 80)
        self.textboxTitlu.resize(280, 40)

        self.textboxEditura.move(70, 140)
        self.textboxEditura.resize(280, 40)

        self.textboxContinut.move(70, 200)
        self.textboxContinut.resize(280, 40)

        self.button.move(50, 350)
        self.button.clicked.connect(self.get_data)

        self.show()

    @pyqtSlot()
    def get_data(self):
        autor = self.textboxAutor.text()
        titlu = self.textboxTitlu.text()
        editura = self.textboxEditura.text()
        continut = self.textboxContinut.text()
        self.library.add_book(autor, titlu, editura, continut)
        self.close()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = LibraryApp()
    window.show()
    sys.exit(app.exec_())
