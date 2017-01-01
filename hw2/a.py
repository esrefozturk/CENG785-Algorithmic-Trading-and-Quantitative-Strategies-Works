from os import listdir
from statistics import stdev, mean
from scipy.stats import linregress
from sklearn import svm, metrics
import numpy as np
from numpy import cov

STOCKS_DIR = 'stocks'
INDICES_DIR = 'indices'


class Entity:

	def print_averages(self):
		with open('averages/%s.csv'%self.name, 'w') as f:
			f.write( '\n'.join( [ str(i) for i in self.averages[::-1] ] ) + '\n' )


	def __init__(self, type=None, file=None):
		if file is None:
			return
		self.type = type
		self.name = file
		self.dates = []
		self.opens = []
		self.highs = []
		self.lows = []
		self.closeds = []
		self.volumes = []
		self.adjusteds = []
		self.averages = []
		self.returns = []
		self.average_return = 0
		self.stdev = 0
		self.alphas = {}
		self.betas = {}

		with open('%ss/%s.csv'%(self.type,file),'r') as f:
			lines = [ i.split(',') for i in f.read().split('\n')[1:-1] ]
		for line in lines:
			self.dates.append(line[0])
			self.opens.append(float(line[1]))
			self.highs.append(float(line[2]))
			self.lows.append(float(line[3]))
			self.closeds.append(float(line[4]))
			self.volumes.append(float(line[5]))
			self.adjusteds.append(float(line[6]))
			self.averages.append( (float(line[1])+float(line[2])+float(line[3])+float(line[4]))/4  )
		for i in range(len(lines)-1):
			self.returns.append( self.averages[i]/self.averages[i+1]-1 )
		self.returns.append(0)
		self.average_return = mean( self.returns[:-1] )
		self.stdev = stdev( self.returns[:-1] )
		self.sharpe_ratio = self.average_return/self.stdev


	def calc_alpha_beta(self, indice):
		#print self.name,indice.name,COV(self.returns[:-1],indice.returns[:-1]),(indice.stdev**2)
		self.betas[indice.name], self.alphas[indice.name], _, _, _ = linregress(indice.returns[:-1],self.returns[:-1])

	def build_portfolio(self, stocks, indices):
		self.returns = []
		self.betas = {}
		self.alphas = {}
		self.name = 'portfolio'

		asdas = 0
		for i in stocks:
			for j in stocks:
				asdas +=cov(i.returns[:-1],j.returns[:-1])[0][1]
		self.stdev = asdas**0.5

		print self.stdev

		'''
		for i in range(len(stocks[0].returns)):
			total = 0
			for s in stocks:
				total += s.returns[i]
			self.returns.append(total/len(stocks))




		self.average_return = mean(self.returns[:-1])
		self.stdev = stdev(self.returns[:-1])
		for indice in indices:
			self.betas[indice.name], self.alphas[indice.name], _, _, _ = linregress(indice.returns[:-1],self.returns[:-1])
		'''


def print_portfolio(filename, stock):
	indices = stock.alphas.keys()
	alphas = [ i.split('-')[1].strip()+'-alpha' for i in indices]
	betas = [ i.split('-')[1].strip()+'-beta' for i in indices]
	header = ['Portfolio','Average Return','Risk'] + alphas + betas
	with open(filename,'w') as f:
		f.write(','.join(header)+'\n')
		lst = [ stock.name,str(stock.average_return),str(stock.stdev) ]
		for indice in indices:
			lst.append( str(stock.alphas[indice]) )
		for indice in indices:
			lst.append( str(stock.betas[indice]) )
		f.write(','.join(lst)+'\n')


def print_csv(filename, stocks):
	indices = stocks[0].alphas.keys()
	alphas = [ i.split('-')[1].strip()+'-alpha' for i in indices]
	betas = [ i.split('-')[1].strip()+'-beta' for i in indices]
	header = ['Stock','Average Return','Risk'] + alphas + betas + ['Sharpe Ratio']
	with open(filename,'w') as f:
		f.write(','.join(header)+'\n')
		for stock in stocks:
			lst = [ stock.name,str(stock.average_return),str(stock.stdev) ]
			for indice in indices:
				lst.append( str(stock.alphas[indice]) )
			for indice in indices:
				lst.append( str(stock.betas[indice]) )
			lst.append(str(stock.sharpe_ratio))
			f.write(','.join(lst)+'\n')





def svm_it(stock, WINDOW_SIZE=4, RATIO=0.8):
	values = stock.averages[::-1]
	m = max(values)
	values = [ int((i/m)*100) for i in values ]
	asd = []
	qwe = []
	for i in range(len(values)-WINDOW_SIZE-1):
		asd.append(values[i:i+WINDOW_SIZE])
		qwe.append(values[i+WINDOW_SIZE])
	SIZE = len(asd)
	S = int(SIZE*RATIO)
	model = svm.SVC()
	model.fit(asd[:S],qwe[:S])
	predicted = model.predict(asd[S:])
	expected = qwe[S:]
	print metrics.confusion_matrix(expected,predicted)
	print metrics.classification_report(expected,predicted)

def main():

	stocks = [ i.split('.')[0] for i in listdir(STOCKS_DIR) ]
	indices = [ i.split('.')[0] for i in listdir(INDICES_DIR) ]

	stocks = [ Entity('stock',i) for i in stocks ]
	indices = [ Entity('indice',i) for i in indices ]

	for stock in stocks:
		stock.print_averages()
		for indice in indices:
				stock.calc_alpha_beta(indice)

	stocks = sorted(stocks,key= lambda stock:-stock.sharpe_ratio)

	#for stock in stocks:
	#	svm_it(stock)

	print_csv("result.csv",stocks)

	portfolio_stocks = stocks[:5]

	print "Stock Counts:"
	for stock in portfolio_stocks:
		stock.count = int(200000/stock.averages[0])

		print '\t',stock.name,':',stock.count,stock.average_return

	portfolio = Entity()
	portfolio.build_portfolio(portfolio_stocks,indices)
	#print_portfolio("portfolio.csv",portfolio)



if __name__ == '__main__':
	main()
