from flask import Flask, render_template, request, redirect
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test.db'
db = SQLAlchemy(app)


class Expense(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    description = db.Column(db.String(200), nullable=False)
    amount = db.Column(db.Integer, nullable=False)
    date_created = db.Column(db.DateTime, default=datetime.utcnow)

    def __repr__(self):
        return '<Expense %r>' % self.id


@app.route('/', methods=['POST', 'GET'])
def index():
    if request.method == 'POST':
        expense_description = request.form['description']
        expense_amount = request.form['amount']
        if expense_amount and expense_description:
            new_expense = Expense(description=expense_description,
                                  amount=expense_amount)

            try:
                db.session.add(new_expense)
                db.session.commit()
                return redirect('/')
            except:
                return 'There was an issue adding your expense'
        else:
            return 'Empty fields!'
    else:
        expenses = Expense.query.order_by(Expense.date_created).all()
        return render_template('index.html', expenses=expenses, total=0)


@app.route('/delete/<int:id>')
def delete(id):
    expense_to_delete = Expense.query.get_or_404(id)

    try:
        db.session.delete(expense_to_delete)
        db.session.commit()
        return redirect('/')
    except:
        return 'There was a problem deleting that expense'


@app.route('/update/<int:id>', methods=['GET', 'POST'])
def update(id):
    expense = Expense.query.get_or_404(id)

    if request.method == 'POST':
        expense_description = request.form['description']
        expense_amount = request.form['amount']
        if expense_description and expense_amount:
            expense.description = expense_description
            expense.amount = expense_amount
            try:
                db.session.commit()
                return redirect('/')
            except:
                return 'There was an issue updating your expense'
        else:
            return 'Empty fields!'
    else:
        return render_template('update.html', expense=expense)


if __name__ == '__main__':
    app.run(debug=True)
