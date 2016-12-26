from os import listdir
from statistics import stdev, mean

STOCKS_DIR = 'stocks'
INDICES_DIR = 'indices'

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

	def calc_alpha_beta(self, indice):
		self.alphas[indice.name] = self.average_return - indice.average_return
		self.betas[indice.name] = self.average_return/indice.average_return





PRICE_DIR = 'prices'
HEADER = 'Date,Open,High,Low,Close,Volume,Adj Close,Average,Return'
DATE_INDEX = 0
OPENN_INDEX = 1
HIGH_INDEX = 2
LOW_INDEX = 3
CLOSE_INDEX = 4
VOLUME_INDEX = 5
ADJ_INDEX = 6
AVG_INDEX = 7
RETURN_INDEX = 8

def get_file_list(path):
	return listdir(path)

def write_file(file, values):
	with open(file,'w') as f:
		f.write(HEADER+'\n')
		for line in values:
			f.write( ','.join( line[:1] + map(str,line[1:]) ) + '\n')


def parse_file(file):
	with open(file) as f:
		lines = [ i.split(',') for i in f.read().split('\n')[1:-1] ]
	newlines = []
	for line in lines:
		date = line[0]
		openn,high,low,close,volume,adj = tuple( map(float,line[1:]) )
		avg  = (openn+high+low+close)/4
		newlines.append([date,openn,high,low,close,volume,adj,avg])

	return newlines

def calc_returns(values):
	for i in range(len(values)-1):
		values[i].append( values[i][AVG_INDEX]/values[i+1][AVG_INDEX]-1 )
	values[-1].append(0)

def print_avg_returns(file,values):
	summ = sum([i[RETURN_INDEX] for i in values[:-1]])
	print file.split('.')[0],': AVERAGE RETURN :',summ/len(values[:-1])

def print_std_devs(file,values):
	returns = [i[RETURN_INDEX] for i in values[:-1]]
	print file.split('.')[0],': STD_DEV :',  stdev(returns)



def main():
	stocks = [ i.split('.')[0] for i in listdir(STOCKS_DIR) ]
	indices = [ i.split('.')[0] for i in listdir(INDICES_DIR) ]

	stocks = [ Entity('stock',i) for i in stocks ]
	indices = [ Entity('indice',i) for i in indices ]

	for stock in stocks:
		for indice in indices:
				stock.calc_alpha_beta(indice)

	



if __name__ == '__main__':
	main()
