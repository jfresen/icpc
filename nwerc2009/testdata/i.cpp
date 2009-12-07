/*
  [NWERC'09] Simple Polygon -- solution checker
  by: Jan Kuipers
*/

#include <iostream>
#include <sstream>
#include <vector>
#include <set>
#include <cmath>
#include <cassert>
#include <algorithm>
#include <cstring>

using namespace std;

const int MAXLEN = 10000000;
const double eps = 1e-9;

struct point {
  int x,y;
  point(): x(0), y(0) {}
  point(int _x, int _y): x(_x), y(_y) {}
};

struct line {
  point p,q;
  line(): p(0,0), q(0,0) {}
};

bool pcomp (line a, line b) { return a.p.x < b.p.x; }
bool qcomp (line a, line b) { return a.q.x < b.q.x; }

int X;

double calcy (line a) {
  if (a.q.x-a.p.x == 0) return a.p.y;
  return 1.0 * ((X-a.p.x)*a.q.y + (a.q.x-X)*a.p.y) / (a.q.x-a.p.x);
}

bool operator < (line a, line b) {
  if (fabs(calcy(a)-calcy(b)) > eps) return calcy(a) < calcy(b);

  double Da = a.q.x==a.p.x ? 1e10 : 1.0 * (a.q.y-a.p.y) / (a.q.x-a.p.x);
  double Db = b.q.x==b.p.x ? 1e10 : 1.0 * (b.q.y-b.p.y) / (b.q.x-b.p.x);

  if (a.p.x<X && b.p.x<X) {
    if (fabs(Da-Db) > eps) return Da>Db;
  }
  else {
    if (fabs(Da-Db) > eps) return Da<Db;
  }  
  
  if (a.p.x != b.p.x)  return a.p.x < b.p.x;
  if (a.p.y != b.p.y)  return a.p.y < b.p.y;
  if (a.q.x != b.q.x)  return a.q.x < b.q.x;
  if (a.q.y != b.q.y)  return a.q.y < b.q.y;

  return false;
}

bool operator == (line a, line b) {
  return a.p.x==b.p.x && a.p.y==b.p.y && a.q.x==b.q.x && a.q.y==b.q.y; 
}

point operator - (point a, point b) { return point(a.x-b.x,a.y-b.y); }
int operator ^ (point a, point b) { return a.x*b.y - b.x*a.y; }
int operator * (point a, point b) { return a.x*b.x + a.y*b.y; }
int sgn (int a) { return a==0 ? 0 : a>0 ? 1 : -1; }

bool intersect (line a, line b) {

  point da = a.q-a.p;
  point db = b.q-b.p;
  
  if ((da^db) != 0) {

    int A = sgn(da^(b.q-a.p)) * sgn(da^(b.p-a.p));
    int B = sgn(db^(a.q-b.p)) * sgn(db^(a.p-b.p));

    if (A<0 && B<=0) return 1;
    if (B<0 && A<=0) return 1;

    return 0;
  }
  else {

    if ((da^(b.p-a.p)) != 0) return 0;
    
    int a1 = a.p * da;
    int a2 = a.q * da;
    if (a1>a2) swap(a1,a2);

    int b1 = b.p * da;
    int b2 = b.q * da;
    if (b1>b2) swap(b1,b2);

    if (a1==b1) return 1;
      
    if (a1>b1) {
      swap(a1,b1);
      swap(a2,b2);
    }

    if (a2>b1) return 1;
    
    return 0;
  }  
}

void printline (line a) {
  printf ("(%i,%i)-(%i,%i)",a.p.x,a.p.y,a.q.x,a.q.y);
}

void error (int run, line a, line b) {
  printf ("Case #%i: ",run);
  printline (a);
  printf (" and ");
  printline (b);
  printf (" intersect.\n");
}


char l[MAXLEN];
int main (int argc, char **argv) {

  if (argc <= 2) {
    printf ("USAGE: %s <testdata.in> <program.out>\n",argv[0]);
    return 1;
  }
  
  FILE *in = fopen (argv[1],"rt");
  FILE *out = fopen (argv[2],"rt");

  int runs;
  fscanf (in,"%d",&runs);

  for (int run=0; run<runs; run++) {

    bool next=false;
    
    int N;
    fscanf (in,"%d",&N);

    vector<point> p(N);
    for (int i=0; i<N; i++) 
      fscanf (in,"%i %i",&p[i].x,&p[i].y);

    if (fgets(l,MAXLEN,out)==NULL) {
      printf ("Not enough lines in output file.\n");
      break;
    }

    int L=strlen(l);

	/*
	if (l[L-1]!='\n') {
		printf ("Case #%i: no newline.\n",run);
		break;
	}
	*/
	
    for (int i=0; i<L; i++)
		if (!isspace(l[i]) && !isdigit(l[i])) {
			printf ("Case #%i: line does not consist of a space separated list of integers.\n",run);
			next=true;
			break;
		}
    if (next) continue;

    istringstream ss(l);
    vector<int> n;
    int t;
    while (ss>>t) n.push_back(t);

    if ((int)n.size()!=N) {
      printf ("Case #%i: line does not contain exactly N integers.\n",run);
      continue;
    }

    vector<int> oldn=n;
    sort(n.begin(),n.end());

    for (int i=0; i<N; i++)
      if (n[i]!=i) {
	printf ("Case #%i: line does not contain a permutation of 0 to N-1.\n",run);
	next=true;
	break;
      }
    if (next) continue;

    n=oldn;
    
    vector<line> add(N);
    for (int i=0; i<N; i++) {
      add[i].p = p[n[i]];
      add[i].q = p[n[(i+1)%N]];
      if (add[i].p.x>add[i].q.x || (add[i].p.x==add[i].q.x && add[i].p.y>add[i].q.y))
	swap(add[i].p,add[i].q);
    }
    sort(add.begin(),add.end(),pcomp);
  
    vector<line> rem=add;
    sort(rem.begin(),rem.end(),qcomp);

    set<line> s;

    int iadd=0,irem=0;

    while (irem<N) {

      if (iadd<N && add[iadd].p.x <= rem[irem].q.x) {

	X = add[iadd].p.x;
	s.insert(add[iadd]);
      
	set<line>::iterator it = s.find(add[iadd]), it2;
      
	if (it!=s.begin()) {
	  it2=it; it2--;
	  if (intersect(*it, *it2)) {
	    error(run,*it,*it2);
	    break;
	  }
	}

	it2=it; it2++;      
	if (it2!=s.end() && intersect(*it, *it2)) {
	  error(run,*it,*it2);
	  break;
	}
      
	iadd++;
      }
      else {

	set<line>::iterator it = s.find(rem[irem]), it2;
	assert (it != s.end());
	
	it2=it; it2++;
	
	if (it!=s.begin() && it2!=s.end()) {
	  it--;
	  if (intersect(*it, *it2)) {
	    error(run,*it,*it2);
	    break;
	  }
	  it++;
	}

	assert (*it == rem[irem]);	
	s.erase(it);

	irem++;
      }
    }
  }

  char bla = getc(out);
 
  if (bla != EOF || !feof(out)) {
    printf ("Too many lines in output file.\n");
  }

  fclose(in);
  fclose(out);
  
  return 0;
}