//
// Created by caiyuan on 2024/10/13.
//
#include<bits/stdc++.h>
using namespace std;
const int N = 10000;
int n;
int v[N];

int mx(int a,int b) {
    if(a==b) {
        cout<<"only one "<<v[a]<<endl;;
        return v[a];
    }
    int mid = (a+b)/2;
    int max1,max2;
    max1 = mx(a,mid);
    max2 = mx(mid+1,b);
    cout<<"max1 = "<<max1<<" "<<"max2 = "<<max2<<endl;
    return max(max1,max2);

}
int mi(int a,int b) {

    if(a==b) return v[a];
    int mid = (a+b)/2;
    int min1,min2;
    min1 = mi(a,mid);
    min2 = mi(mid+1,b);
    return min(min1,min2);
}


int main() {
    cin>>n;
    for(int i=1;i<=n;i++) cin>>v[i];

    cout<<"max:"<<mx(1,n)<<endl;
    cout<<"min:"<<mi(1,n)<<endl;

    return 0;
}