kubectl apply -f mysql-deploy.yaml
sleep 2
kubectl apply -f mysql-service.yaml
sleep 2
kubectl apply -f backend-app-deploy.yaml
sleep 2
kubectl apply -f backend-app-service.yaml
sleep 3
kubectl apply -f frontend-app-service.yaml
sleep 3
kubectl apply -f frontend-app-service.yaml