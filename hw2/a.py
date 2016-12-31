from os import listdir
from statistics import stdev, mean

STOCKS_DIR = 'stocks'
INDICES_DIR = 'indices'

def abs(x):
	return x if x>0 else -(x)

def COV(x, y):
	mx = mean(x)
	my = mean(y)
	n = len(x)
	s = 0
	for i in range(n):
		s += abs(x[i]-mx)*abs(y[i]-my)
	return s/n

class Entity:

	def __init__(self, type, file):
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
		self.betas[indice.name] = COV(self.returns[:-1],indice.returns[:-1])/(indice.stdev**2)
		self.alphas[indice.name] = self.average_return - self.betas[indice.name]*indice.average_return

def print_csv(filename, stocks):
	indices = stocks[0].alphas.keys()
	alphas = [ i.split('-')[1].strip()+'-alpha' for i in indices]
	betas = [ i.split('-')[1].strip()+'-beta' for i in indices]
	header = ['Stock','Average Return','Risk'] + alphas + betas + ['Sharpe Ratio']
	stocks = sorted(stocks,key= lambda stock:-stock.sharpe_ratio)
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




def main():

	stocks = [ i.split('.')[0] for i in listdir(STOCKS_DIR) ]
	indices = [ i.split('.')[0] for i in listdir(INDICES_DIR) ]

	stocks = [ Entity('stock',i) for i in stocks ]
	indices = [ Entity('indice',i) for i in indices ]

	for stock in stocks:
		for indice in indices:
				stock.calc_alpha_beta(indice)

	print_csv("result.csv",stocks)




if __name__ == '__main__':
	main()
