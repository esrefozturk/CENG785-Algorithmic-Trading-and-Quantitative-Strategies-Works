from os import listdir
PRICE_DIR = 'prices'
HEADER = 'Date,Open,High,Low,Close,Volume,Adj Close,Average'

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


def main():
	files = get_file_list(PRICE_DIR)
	for file in files:
		values = parse_file('%s/%s'%(PRICE_DIR,file))
		write_file('new/'+file.split('.')[0]+'.new.csv',values)


if __name__ == '__main__':
	main()
