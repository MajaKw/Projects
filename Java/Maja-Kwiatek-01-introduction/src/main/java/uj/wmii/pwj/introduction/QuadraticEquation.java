package uj.wmii.pwj.introduction;

public class QuadraticEquation {

    public double[] findRoots(double a, double b, double c) {
        double delta = b * b - 4 * a * c;

        if(delta < 0){
            return new double[]{};
        } else if (delta == 0){
            double root = -b / (2*a);
            return new double[]{root};
        }else{
            double rootOne = (-b + Math.sqrt(delta)) / (2 * a);
            double rootTwo = (-b - Math.sqrt(delta)) / (2 * a);
            return new double[]{rootOne, rootTwo};
        }
    }

}

