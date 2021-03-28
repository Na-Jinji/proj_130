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
경주 관광지 추천 시스템은 사용자가 하나의 관광지명을 입력하면 이와 유사한 5개의 경주 관광지들을 추천해주는 추천 시스템입니다. 경주 관광지 추천 시스템은 flask 라이브러리를 사용하여 웹서버로 구동하고 있습니다.   
* 경주 관광지 추천 시스템 : https://github.com/Na-Jinji/ai-server   
<br>

## [ 추천 시스템 로직 ]
각각 경주 관광지의 특징들을 명사(noun), 대명사(pronoun), 동사(verb)로 표현한 특징 리스트를 생성하여 data.csv 파일로 저장하였습니다. 
추천 시스템을 생성할 때 csv 파일에서 '카테고리(category)', '관광지명(title)', '특징 리스트(tag)'를 추출합니다.
관광지마다 해당 카테고리와 특징 리스트를 하나의 text 문장으로 합친 뒤에 **Scikit-Learn의 CountVectorizer**를 통하여 문장 간 유사도를 구합니다. 
*CountVectorizer*는 문서 목록에서 각 문서의 feature (문장의 특징) 노출수를 가중치로 설정한 BOW(Bag Of Words) 벡터를 생성하는 API입니다.
*CountVectorizer의 fit_tranform*을 호출하여 각 관광지의 문장에서 노출되는 feature 수를 합한 DTM(Document Term Matrix)를 생성합니다. 여기서 경주 관광지들의 DTM은 shape가 (330, 878)입니다. 이 DTM을 이용하여 코사인 유사도(cosine similarity)를 구하면 get_recommendations이 호출될 때마다 코사인 유사도 중 해당 관광지명 인덱스에 해당하는 값에서 추천 개수만큼 뽑아냅니다. 이때 본인 관광지는 제외됩니다. 

```python
    class Model:
        def __init__(self, tokenizer=None):
            gyeongju_data = pd.read_csv('model/data.csv')
            self.metaData = gyeongju_data[['category', 'title', 'tag']].drop_duplicates()
            self.metaData['soup'] = self.metaData.apply(create_soup, axis=1)

            # 이름:index - 예) 로라커피:0, 이스트앵글:1
            self.indices = pd.Series(self.metaData.index, index=self.metaData['title']).drop_duplicates()

            # BOW 인코딩
            if tokenizer:
                count = CountVectorizer(analyzer='word', tokenizer=tokenizer.morphs)
            else:
                count = CountVectorizer(analyzer='word')
            count_matrix = count.fit_transform(self.metaData['soup'])

            # 코사인 유사도 구하기
            self.cosine_sim2 = cosine_similarity(count_matrix, count_matrix)

            # index 초기화
            self.metaData = self.metaData.reset_index()
            self.indices = pd.Series(self.metaData.index, index=self.metaData['title'])
```
<br>

### get_recommendations의 기본 로직은 이렇습니다.
1. 관광지명이 들어오면 관광지 이름을 가지고 있는 index를 뽑아냅니다. 
2. 코사인 유사도 중 관광지 이름에 해당하는 리스트인 sim_scores를 추출합니다. 
3. sim_scores를 내림차순으로 정렬합니다.
4. sim_scores에서 본인을 제외한 상위 5개를 뽑아낸 뒤 해당 score의 관광지명들을 리턴합니다. 

```python
   # 5개의 추천리스트 가져오기
    def get_recommendations(self, title):
        title = self.set_exact_title(title)
        if len(title) <= 0 or title == '':
            return []

        idx = self.indices[title]
        sim_scores = list(enumerate(self.cosine_sim2[idx]))  # 유사도 측정
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)  # 내림차순

        sim_scores = sim_scores[1:6]  # 5개
        attraction_indices = [i[0] for i in sim_scores]     # 장소 index
        scores = [i[1] for i in sim_scores]     # 유사도

        # debug
        for i in scores:
            print(i)

        result_data = self.metaData[['title', 'tag']].iloc[attraction_indices]
        result_data['scores'] = np.array(scores)
        return result_data['title'].values.tolist()
```
<br><br>
 
# 5. Server

https://github.com/nayoon-kim/ohmymoney-server

# 6. Source
* 경주 관광지의 이미지들은 구글 썸네일 이미지로부터 수집하였습니다.
* 경주 관광지의 카테고리(카페,명소,음식), 관광지명, 주소, 위치 정보(위도,경도), 전화번호, 홈페이지주소, 상세정보는 경주시청에서 운영하는 경주문화관광 웹사이트에서 수집하였습니다. 
>  경주문화관광 : https://www.gyeongju.go.kr/tour/index.do
<br><br>

# 7. Youtube


# 8. People
- 김나윤: https://github.com/nayoon-kim
- 김진희: https://github.com/jinhee19
- 설지우: https://github.com/jeewoo1025
