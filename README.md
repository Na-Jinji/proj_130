# 1. Introduction


# 2. Development Stack
- Python 3.7
- Flask
- Android Studio
- Docker
- Spring Boot
- MySQL
- Nginx


# 3. Features
### Search & Display
### Bookmark
### MyPage


# 4. Recommendation System
경주 관광지 추천 시스템은 사용자가 하나의 관광지명을 입력하면 이와 유사한 5개의 경주 관광지들을 추천해주는 추천 시스템입니다. 
경주 관광지 추천 시스템은 flask 라이브러리를 사용하여 웹서버로 구동하고 있습니다.

* 경주 관광지 추천 시스템 : https://github.com/Na-Jinji/ai-server   
<br><br>

## [ 추천 시스템 로직 ]
각각 경주 관광지의 특징들을 명사(noun), 대명사(pronoun), 동사(verb)로 표현한 특징 리스트를 생성하여 data.csv 파일로 저장하였습니다. 
추천 시스템을 생성할 때 csv 파일에서 '카테고리(category)', '관광지명(title)', '특징 리스트(tag)'를 추출합니다.
관광지마다 해당 카테고리와 특징 리스트를 하나의 text 문장으로 합친 뒤에 **Scikit-Learn의 CountVectorizer**를 통하여 문장 간 유사도를 구합니다. 
*CountVectorizer*는 문서 목록에서 각 문서의 feature (문장의 특징) 노출수를 가중치로 설정한 BOW(Bag Of Words) 벡터를 생성하는 API입니다.
*CountVectorizer의 fit_tranform*을 호출하여 각 관광지의 문장에서 노출되는 feature 수를 합한 DTM(Document Term Matrix)를 생성합니다. 여기서 경주 관광지들의 DTM은 shape가 (330, 878)입니다. 이 DTM을 이용하여 코사인 유사도(cosine similarity)를 구하면 get_recommendations이 호출될 때마다 코사인 유사도 중 해당 관광지명 인덱스에 해당하는 값에서 추천 개수만큼 뽑아냅니다. 이때 본인 관광지는 제외됩니다. 


### get_recommendations의 기본 로직은 이렇습니다.
1. 관광지명이 들어오면 관광지 이름을 가지고 있는 index를 뽑아냅니다. 
2. 코사인 유사도 중 관광지 이름에 해당하는 리스트인 sim_scores를 추출합니다. 
3. sim_scores를 내림차순으로 정렬합니다.
4. sim_scores에서 본인을 제외한 상위 5개를 뽑아낸 뒤 해당 score의 관광지명들을 리턴합니다. 
<br><br>
 
# 5. Server

https://github.com/nayoon-kim/ohmymoney-server

# 6. Source


# 7. Youtube


# 8. People
- 김나윤: https://github.com/nayoon-kim
- 김진희: https://github.com/jinhee19
- 설지우: https://github.com/jeewoo1025
